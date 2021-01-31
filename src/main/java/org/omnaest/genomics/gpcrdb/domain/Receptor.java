package org.omnaest.genomics.gpcrdb.domain;

import java.util.Set;

public interface Receptor
{
    public String getName();

    public Set<String> getGenes();

    public String getNameHtmlEncoded();

    public String getUniprotId();

}
