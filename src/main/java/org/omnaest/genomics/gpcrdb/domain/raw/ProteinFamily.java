package org.omnaest.genomics.gpcrdb.domain.raw;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProteinFamily
{
    @JsonProperty
    private String        slug;
    @JsonProperty
    private String        name;
    @JsonProperty
    private ProteinFamily parent;

    public String getSlug()
    {
        return this.slug;
    }

    public String getName()
    {
        return this.name;
    }

    public ProteinFamily getParent()
    {
        return this.parent;
    }

    @Override
    public String toString()
    {
        return "ProteinFamily [slug=" + this.slug + ", name=" + this.name + ", parent=" + this.parent + "]";
    }

}
