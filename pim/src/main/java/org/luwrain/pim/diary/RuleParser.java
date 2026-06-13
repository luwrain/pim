// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary;

import java.util.*;
import java.time.*;
import java.time.format.*;

/**
 * Parses and represents an iCalendar recurrence rule
 * (<a href="https://tools.ietf.org/html/rfc5545#section-3.3.10">RRULE</a>).
 *
 * <p>An RRULE string defines how a calendar event repeats over time.
 * Example: {@code FREQ=WEEKLY;BYDAY=MO,WE,FR;COUNT=24} means the event
 * occurs every week on Monday, Wednesday and Friday, 24 times.</p>
 *
 * <p>Usage:</p>
 * <pre>{@code
 * var rule = new RuleParser("FREQ=WEEKLY;BYDAY=MO,WE;INTERVAL=2");
 * if (rule.isValid()) {
 *     System.out.println("Frequency: " + rule.getFrequency());
 *     for (var day : rule.getByDay())
 *         System.out.println("  Day: " + day);
 * }
 * }</pre>
 *
 * <p>The parser is lenient: unknown parameters are silently ignored,
 * but syntactic errors cause {@link #isValid()} to return {@code false}.
 * After parsing, all getters return the parsed values, or safe defaults
 * where applicable.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc5545#section-3.3.10">RFC 5545, Section 3.3.10</a>
 */
public final class RuleParser
{
    // ── Frequency enum ──────────────────────────────────────────────

    /**
     * Recurrence frequency as specified by the {@code FREQ} parameter.
     */
    public enum Frequency
    {
	/** Every day. */
	DAILY,
	/** Every week. */
	WEEKLY,
	/** Every month. */
	MONTHLY,
	/** Every year. */
	YEARLY;

	/**
	 * Resolves a case-insensitive string to a {@link Frequency} value.
	 *
	 * @param s the FREQ parameter value (e.g. {@code "WEEKLY"})
	 * @return the corresponding frequency, or {@code null} if unknown
	 */
	public static Frequency of(String s)
	{
	    if (s == null)
		return null;
	    try {
		return valueOf(s.toUpperCase());
	    }
	    catch (IllegalArgumentException e)
	    {
		return null;
	    }
	}
    }

    // ── DayOfWeek wrapper for BYDAY ─────────────────────────────────

    /**
     * Represents a single BYDAY entry: an optional ordinal week number
     * and a day of the week. For example, {@code "2TU"} means the second
     * Tuesday, {@code "-1FR"} means the last Friday, and {@code "MO"}
     * means every Monday.
     */
    public static final class ByDay
    {
	/**
	 * Ordinal week position within a month or year. Positive values
	 * count from the start (1 = first, 2 = second), negative from the
	 * end (-1 = last). Zero means every occurrence of this day.
	 */
	public final int ordinal;

	/**
	 * Day of the week.
	 */
	public final DayOfWeek day;

	/**
	 * Creates a new ByDay entry.
	 *
	 * @param ordinal ordinal position (0 for every occurrence,
	 *                positive for nth from start, negative for nth
	 *                from end)
	 * @param day day of the week, must not be {@code null}
	 * @throws NullPointerException if {@code day} is {@code null}
	 */
	public ByDay(int ordinal, DayOfWeek day)
	{
	    this.ordinal = ordinal;
	    this.day = Objects.requireNonNull(day, "day can't be null");
	}

	/**
	 * Parses a BYDAY value token such as {@code "MO"}, {@code "2TU"}
	 * or {@code "-1FR"}.
	 *
	 * @param token the token to parse
	 * @return the parsed {@link ByDay} entry, or {@code null} if the
	 *         token could not be recognised
	 */
	public static ByDay parse(String token)
	{
	    if (token == null || token.isEmpty())
		return null;
	    final String s = token.trim().toUpperCase();
	    int ordinal = 0;
	    int pos = 0;
	    // Parse optional sign
	    boolean negative = false;
	    if (s.charAt(0) == '-')
	    {
		negative = true;
		pos = 1;
	    }
	    else if (s.charAt(0) == '+')
	    {
		pos = 1;
	    }
	    // Parse digits
	    if (pos < s.length() && Character.isDigit(s.charAt(pos)))
	    {
		final var sb = new StringBuilder();
		while (pos < s.length() && Character.isDigit(s.charAt(pos)))
		{
		    sb.append(s.charAt(pos));
		    ++pos;
		}
		try {
		    ordinal = Integer.parseInt(sb.toString());
		}
		catch (NumberFormatException e)
		{
		    return null;
		}
		if (negative)
		    ordinal = -ordinal;
	    }
	    // The rest is the day abbreviation
	    final String dayStr = s.substring(pos);
	    final DayOfWeek day = parseDayOfWeek(dayStr);
	    if (day == null)
		return null;
	    return new ByDay(ordinal, day);
	}

	private static DayOfWeek parseDayOfWeek(String s)
	{
	    if (s == null || s.isEmpty())
		return null;
	    return switch (s.toUpperCase())
	    {
		case "SU" -> DayOfWeek.SUNDAY;
		case "MO" -> DayOfWeek.MONDAY;
		case "TU" -> DayOfWeek.TUESDAY;
		case "WE" -> DayOfWeek.WEDNESDAY;
		case "TH" -> DayOfWeek.THURSDAY;
		case "FR" -> DayOfWeek.FRIDAY;
		case "SA" -> DayOfWeek.SATURDAY;
		default -> null;
	    };
	}

	/**
	 * Returns the string representation as used in an RRULE value
	 * (e.g. {@code "MO"}, {@code "2TU"}, {@code "-1FR"}).
	 *
	 * @return the BYDAY token
	 */
	@Override public String toString()
	{
	    final var sb = new StringBuilder();
	    if (ordinal != 0)
		sb.append(ordinal);
	    sb.append(dayAbbreviation(day));
	    return sb.toString();
	}

	static String dayAbbreviation(DayOfWeek d)
	{
	    return switch (d)
	    {
		case SUNDAY -> "SU";
		case MONDAY -> "MO";
		case TUESDAY -> "TU";
		case WEDNESDAY -> "WE";
		case THURSDAY -> "TH";
		case FRIDAY -> "FR";
		case SATURDAY -> "SA";
	    };
	}

	@Override public boolean equals(Object o)
	{
	    if (o != null && o instanceof ByDay b)
		return ordinal == b.ordinal && day == b.day;
	    return false;
	}

	@Override public int hashCode()
	{
	    return Objects.hash(ordinal, day);
	}
    }

    // ── Instance fields ──────────────────────────────────────────────

    private final String source;
    private boolean valid = true;

    private Frequency freq;
    private int interval = 1;
    private Integer count;
    private LocalDate untilDate;
    private List<ByDay> byDay = List.of();
    private List<Integer> byMonthDay = List.of();
    private List<Integer> byMonth = List.of();
    private List<Integer> byYearDay = List.of();
    private List<Integer> byWeekNo = List.of();
    private List<Integer> bySetPos = List.of();
    private DayOfWeek weekStart = DayOfWeek.MONDAY;

    // ── Constructor ─────────────────────────────────────────────────

    /**
     * Parses an RRULE string.
     *
     * @param rrule the raw RRULE value (e.g.
     *              {@code "FREQ=WEEKLY;BYDAY=MO,WE,FR;COUNT=24"}).
     *              May be {@code null} or empty — the result will be
     *              marked invalid via {@link #isValid()}.
     */
    public RuleParser(String rrule)
    {
	this.source = rrule;
	if (rrule == null || rrule.isBlank())
	{
	    this.valid = false;
	    return;
	}
	parse(rrule.trim());
    }

    // ── Parsing ─────────────────────────────────────────────────────

    private void parse(String input)
    {
	final String[] parts = input.split(";");
	for (final String part : parts)
	{
	    final int eq = part.indexOf('=');
	    if (eq < 0)
		continue;
	    final String key = part.substring(0, eq).trim().toUpperCase();
	    final String value = part.substring(eq + 1).trim();
	    if (value.isEmpty())
		continue;

	    try {
		parseParam(key, value);
	    }
	    catch (Exception e)
	    {
		this.valid = false;
	    }
	}
	if (freq == null)
	    this.valid = false;
    }

    private void parseParam(String key, String value)
    {
	switch (key)
	{
	case "FREQ":
	    freq = Frequency.of(value);
	    if (freq == null)
		valid = false;
	    break;
	case "INTERVAL":
	    interval = Integer.parseInt(value);
	    if (interval < 1)
		valid = false;
	    break;
	case "COUNT":
	    count = Integer.parseInt(value);
	    if (count < 1)
		valid = false;
	    break;
	case "UNTIL":
	    untilDate = parseUntil(value);
	    if (untilDate == null)
		valid = false;
	    break;
	case "BYDAY":
	    byDay = parseByDayList(value);
	    break;
	case "BYMONTHDAY":
	    byMonthDay = parseIntList(value, -31, 31);
	    break;
	case "BYMONTH":
	    byMonth = parseIntList(value, 1, 12);
	    break;
	case "BYYEARDAY":
	    byYearDay = parseIntList(value, -366, 366);
	    break;
	case "BYWEEKNO":
	    byWeekNo = parseIntList(value, -53, 53);
	    break;
	case "BYSETPOS":
	    bySetPos = parseIntList(value, -366, 366);
	    break;
	case "WKST":
	    weekStart = parseDayOfWeek(value);
	    if (weekStart == null)
		valid = false;
	    break;
	    // Unknown keys are silently ignored
	}
    }

    // ── Low-level parsing helpers ────────────────────────────────────

    private static LocalDate parseUntil(String value)
    {
	// Two formats: DATE-TIME (yyyyMMdd'T'HHmmss) or DATE (yyyyMMdd)
	try {
	    final String clean = value.replace("Z", "").trim();
	    if (clean.contains("T"))
	    {
		return LocalDate.parse(clean.substring(0, 8),
				       DateTimeFormatter.ofPattern("yyyyMMdd"));
	    }
	    return LocalDate.parse(clean, DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	catch (DateTimeParseException e)
	{
	    return null;
	}
    }

    static List<ByDay> parseByDayList(String value)
    {
	final var list = new ArrayList<ByDay>();
	for (final String token : value.split(","))
	{
	    final var bd = ByDay.parse(token.trim());
	    if (bd != null)
		list.add(bd);
	}
	return List.copyOf(list);
    }

    static List<Integer> parseIntList(String value, int min, int max)
    {
	final var list = new ArrayList<Integer>();
	for (final String token : value.split(","))
	{
	    try {
		final int v = Integer.parseInt(token.trim());
		if (v >= min && v <= max && v != 0)
		    list.add(v);
	    }
	    catch (NumberFormatException e)
	    {
		// skip
	    }
	}
	return List.copyOf(list);
    }

    static DayOfWeek parseDayOfWeek(String s)
    {
	if (s == null || s.isEmpty())
	    return null;
	return switch (s.toUpperCase())
	{
	    case "SU" -> DayOfWeek.SUNDAY;
	    case "MO" -> DayOfWeek.MONDAY;
	    case "TU" -> DayOfWeek.TUESDAY;
	    case "WE" -> DayOfWeek.WEDNESDAY;
	    case "TH" -> DayOfWeek.THURSDAY;
	    case "FR" -> DayOfWeek.FRIDAY;
	    case "SA" -> DayOfWeek.SATURDAY;
	    default -> null;
	};
    }

    // ── Public getters ───────────────────────────────────────────────

    /**
     * Returns the original RRULE string that was passed to the
     * constructor.
     *
     * @return the raw RRULE source, or {@code null} if none was provided
     */
    public String getSource()
    {
	return source;
    }

    /**
     * Indicates whether the RRULE string was successfully parsed.
     *
     * @return {@code true} if the RRULE is syntactically valid and has
     *         at least a FREQ parameter
     */
    public boolean isValid()
    {
	return valid;
    }

    /**
     * Returns the recurrence frequency. Never {@code null} when
     * {@link #isValid()} returns {@code true}.
     *
     * @return the FREQ value, or {@code null} if not set
     */
    public Frequency getFrequency()
    {
	return freq;
    }

    /**
     * Returns the recurrence interval (the value of INTERVAL, or 1
     * if the parameter was omitted).
     *
     * @return interval, at least 1
     */
    public int getInterval()
    {
	return interval;
    }

    /**
     * Returns the maximum number of occurrences (COUNT parameter).
     *
     * @return the count, or {@code null} if COUNT was not specified
     */
    public Integer getCount()
    {
	return count;
    }

    /**
     * Returns the date limit for recurrences (UNTIL parameter).
     *
     * @return the until date (date part only, ignoring time), or
     *         {@code null} if UNTIL was not specified
     */
    public LocalDate getUntilDate()
    {
	return untilDate;
    }

    /**
     * Returns the list of BYDAY entries specifying which days of the
     * week the recurrence applies to.
     *
     * @return immutable list of {@link ByDay}, never {@code null}
     */
    public List<ByDay> getByDay()
    {
	return byDay;
    }

    /**
     * Returns the list of month days (BYMONTHDAY) specifying which
     * days of the month the recurrence applies to.
     *
     * @return immutable list, values from -31 to 31 (except 0),
     *         never {@code null}
     */
    public List<Integer> getByMonthDay()
    {
	return byMonthDay;
    }

    /**
     * Returns the list of months (BYMONTH) specifying which months of
     * the year the recurrence applies to.
     *
     * @return immutable list, values from 1 to 12, never {@code null}
     */
    public List<Integer> getByMonth()
    {
	return byMonth;
    }

    /**
     * Returns the list of year days (BYYEARDAY).
     *
     * @return immutable list, values from -366 to 366 (except 0),
     *         never {@code null}
     */
    public List<Integer> getByYearDay()
    {
	return byYearDay;
    }

    /**
     * Returns the list of week numbers (BYWEEKNO).
     *
     * @return immutable list, values from -53 to 53 (except 0),
     *         never {@code null}
     */
    public List<Integer> getByWeekNo()
    {
	return byWeekNo;
    }

    /**
     * Returns the list of set positions (BYSETPOS).
     *
     * @return immutable list, values from -366 to 366 (except 0),
     *         never {@code null}
     */
    public List<Integer> getBySetPos()
    {
	return bySetPos;
    }

    /**
     * Returns the week start day (WKST).
     *
     * @return week start day, defaults to {@link DayOfWeek#MONDAY}
     */
    public DayOfWeek getWeekStart()
    {
	return weekStart;
    }

    // ── Canonical form ───────────────────────────────────────────────

    /**
     * Reconstructs the canonical (normalised) RRULE string from the
     * parsed parameters. The output may differ from the original source
     * (e.g. in parameter order, case, or spacing), but it is semantically
     * equivalent.
     *
     * <p>Returns the raw source string if the rule was never successfully
     * parsed.</p>
     *
     * @return a canonical RRULE string, or the original source if
     *         {@link #isValid()} is {@code false}
     */
    @Override public String toString()
    {
	if (!valid)
	    return source != null ? source : "";
	final var sb = new StringBuilder();
	sb.append("FREQ=").append(freq.name());
	if (interval != 1)
	    sb.append(";INTERVAL=").append(interval);
	if (count != null)
	    sb.append(";COUNT=").append(count);
	if (untilDate != null)
	    sb.append(";UNTIL=").append(untilDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
	if (!byDay.isEmpty())
	{
	    sb.append(";BYDAY=");
	    for (int i = 0; i < byDay.size(); ++i)
	    {
		if (i > 0) sb.append(",");
		sb.append(byDay.get(i));
	    }
	}
	if (!byMonthDay.isEmpty())
	{
	    sb.append(";BYMONTHDAY=");
	    appendIntList(sb, byMonthDay);
	}
	if (!byMonth.isEmpty())
	{
	    sb.append(";BYMONTH=");
	    appendIntList(sb, byMonth);
	}
	if (!byYearDay.isEmpty())
	{
	    sb.append(";BYYEARDAY=");
	    appendIntList(sb, byYearDay);
	}
	if (!byWeekNo.isEmpty())
	{
	    sb.append(";BYWEEKNO=");
	    appendIntList(sb, byWeekNo);
	}
	if (!bySetPos.isEmpty())
	{
	    sb.append(";BYSETPOS=");
	    appendIntList(sb, bySetPos);
	}
	if (weekStart != DayOfWeek.MONDAY)
	    sb.append(";WKST=").append(ByDay.dayAbbreviation(weekStart));
	return sb.toString();
    }

    static private void appendIntList(StringBuilder sb, List<Integer> list)
    {
	for (int i = 0; i < list.size(); ++i)
	{
	    if (i > 0) sb.append(",");
	    sb.append(list.get(i));
	}
    }
}
