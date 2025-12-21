// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

@Data
public class MessageMetadata implements Serializable
{
    public enum State {NEW, READ, MARKED, DELETED};

    private long id;
    private State state;
    private int folderId;
    private String messageId, subject, fromAddr, content;
    private long sentTimestamp;
    private List<String> toAddr, ccAddr;
    private transient String title;
}
