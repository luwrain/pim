
package org.luwrain.pim;

public interface StoredCommonAttr
{
    String[] getTags() throws Exception;
    void setTags(String[] value) throws Exception;
    String[] getUniRefs() throws Exception;
    void setUniRefs(String[] value) throws Exception;
    String getNotes() throws Exception;
    void setNotes(String value) throws Exception;
}
