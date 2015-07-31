
package org.luwrain.pim.binder;

public interface BinderStoring
{
    StoredCase[] getCases() throws Exception;
    void saveCase(Case c) throws Exception;
    void deleteCase(StoredCase c) throws Exception;
}
