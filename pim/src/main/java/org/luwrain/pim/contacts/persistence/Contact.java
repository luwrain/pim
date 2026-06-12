// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

@Data
public class Contact implements Serializable
{
    private long id;

    // ID of the parent ContactsFolder, -1 if this contact is in the root
    private long parentFolderId = -1;

    // FN
    private String formattedName;

    // N
    private String familyName, givenName, middleName, prefix, suffix;

    // NICKNAME
    private String nickname;

    // TEL
    private List<Phone> phones;

    // EMAIL
    private List<Email> emails;

    // ADR
    private List<Address> addresses;

    // ORG
    private String organization;

    // TITLE
    private String title;

    // ROLE
    private String role;

    // NOTE
    private String note;

    // URL
    private String url;

    // BDAY (ISO-8601: YYYY-MM-DD)
    private String birthday;

    // PHOTO URL
    private String photoUrl;

    // CATEGORIES
    private List<String> categories;

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Contact c)
	    return id == c.id;
	return false;
    }

    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    @Override public String toString()
    {
	return formattedName != null?formattedName:"";
    }
}
