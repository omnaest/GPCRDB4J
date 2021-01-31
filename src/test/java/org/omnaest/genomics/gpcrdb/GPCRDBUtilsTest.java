package org.omnaest.genomics.gpcrdb;

import org.junit.Ignore;
import org.junit.Test;
import org.omnaest.genomics.gpcrdb.GPCRDBUtils;
import org.omnaest.genomics.gpcrdb.GPCRDBUtils.SpeciesGPCRDBDataAccessor;

public class GPCRDBUtilsTest
{

    @Test
    @Ignore
    public void testGetInstance() throws Exception
    {
        SpeciesGPCRDBDataAccessor accessor = GPCRDBUtils.getInstance()
                                                        .usingLocalCache()
                                                        .forHuman();

        accessor.getReceptors()
                .forEach(receptor ->
                {
                    String name = receptor.getName();
                    System.out.println(name);
                });

    }

}
