
package org.luwrain.pim.news;

import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

public interface NewsGroups
{
    StoredNewsGroup[] load() throws PimException;
    StoredNewsGroup loadById(long id) throws PimException;
    void save(NewsGroup group) throws PimException;
    void delete(StoredNewsGroup group) throws PimException;
}
