package org.omnaest.genomics.gpcrdb;

import org.junit.Ignore;
import org.junit.Test;
import org.omnaest.genomics.gpcrdb.GPCRDBRestAPIUtils;
import org.omnaest.genomics.gpcrdb.domain.raw.ProteinFamilies;
import org.omnaest.utils.JSONHelper;

public class GPCRDBRestAPIUtilsTest
{

    @Test
    @Ignore
    public void testGetInstance() throws Exception
    {
        ProteinFamilies proteinFamilies = GPCRDBRestAPIUtils.getInstance()
                                                            .usingLocalCache()
                                                            .getProteinFamilies();
        System.out.println(JSONHelper.prettyPrint(proteinFamilies));
    }

}
