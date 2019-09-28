
package org.luwrain.pim;

public interface StoredCommonAttr
{
    String[] getTags() throws PimException;
    void setTags(String[] value) throws PimException;
    String[] getUniRefs() throws PimException;
    void setUniRefs(String[] value) throws PimException;
    String getNotes() throws PimException;
    void setNotes(String value) throws PimException;
}
