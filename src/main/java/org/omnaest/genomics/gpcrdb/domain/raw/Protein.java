package org.omnaest.genomics.gpcrdb.domain.raw;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Protein
{
    @JsonProperty("entry_name")
    private String      entryName;
    @JsonProperty
    private String      name;
    @JsonProperty
    private String      accession;
    @JsonProperty
    private String      family;
    @JsonProperty
    private String      species;
    @JsonProperty
    private String      source;
    @JsonProperty("residue_numbering_scheme")
    private String      residueNumberScheme;
    @JsonProperty
    private String      sequence;
    @JsonProperty
    private Set<String> genes = new LinkedHashSet<>();

    public String getEntryName()
    {
        return this.entryName;
    }

    public String getName()
    {
        return this.name;
    }

    public String getAccession()
    {
        return this.accession;
    }

    public String getFamily()
    {
        return this.family;
    }

    public String getSpecies()
    {
        return this.species;
    }

    public String getSource()
    {
        return this.source;
    }

    public String getResidueNumberScheme()
    {
        return this.residueNumberScheme;
    }

    public String getSequence()
    {
        return this.sequence;
    }

    public Set<String> getGenes()
    {
        return this.genes;
    }

    @Override
    public String toString()
    {
        return "Protein [entryName=" + this.entryName + ", name=" + this.name + ", accession=" + this.accession + ", family=" + this.family + ", species="
                + this.species + ", source=" + this.source + ", residueNumberScheme=" + this.residueNumberScheme + ", sequence=" + this.sequence + ", genes="
                + this.genes + "]";
    }

}
