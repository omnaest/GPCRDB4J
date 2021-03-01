/*******************************************************************************
 * Copyright 2021 Danny Kunz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.omnaest.genomics.gpcrdb;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.omnaest.genomics.gpcrdb.GPCRDBRestAPIUtils.GPCRRestAccessor;
import org.omnaest.genomics.gpcrdb.domain.Receptor;
import org.omnaest.genomics.gpcrdb.domain.raw.Protein;
import org.omnaest.genomics.gpcrdb.domain.raw.ProteinFamily;
import org.omnaest.utils.CollectorUtils;
import org.omnaest.utils.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GPCRDBUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(GPCRDBUtils.class);

    public static interface GPCRDBDataAccessor
    {
        public SpeciesGPCRDBDataAccessor forSpecies(String species);

        public SpeciesGPCRDBDataAccessor forHuman();

        public GPCRDBDataAccessor usingCache(Cache cache);

        public GPCRDBDataAccessor usingLocalCache();
    }

    public static interface SpeciesGPCRDBDataAccessor
    {
        public Stream<Receptor> getReceptors();

    }

    public static GPCRDBDataAccessor getInstance()
    {
        return new GPCRDBDataAccessor()
        {
            private GPCRRestAccessor restAccessor = GPCRDBRestAPIUtils.getInstance();

            @Override
            public SpeciesGPCRDBDataAccessor forSpecies(String species)
            {
                return new SpeciesGPCRDBDataAccessor()
                {
                    private Map<String, Protein> nameToProtein = this.createNameToProteinMap(species);

                    @Override
                    public Stream<Receptor> getReceptors()
                    {

                        return this.nameToProtein.entrySet()
                                                 .stream()
                                                 .map(entry -> this.createReceptor(this.normalizeName(entry.getKey()), entry.getValue()));

                    }

                    private Map<String, Protein> createNameToProteinMap(String species)
                    {
                        Map<String, Set<String>> parentToChildren = new HashMap<>();
                        Map<String, ProteinFamily> nameToFamily = new HashMap<>();
                        restAccessor.getProteinFamilies()
                                    .forEach(family ->
                                    {
                                        //
                                        String name = family.getName();
                                        nameToFamily.put(name, family);

                                        //
                                        ProteinFamily parent = family.getParent();
                                        if (parent != null)
                                        {
                                            String parentName = parent.getName();
                                            if (parentName != null)
                                            {
                                                parentToChildren.computeIfAbsent(parentName, pn -> new HashSet<>())
                                                                .add(name);
                                            }
                                        }
                                    });

                        Set<String> recptorNames = nameToFamily.keySet()
                                                               .stream()
                                                               .filter(name -> parentToChildren.getOrDefault(name, Collections.emptySet())
                                                                                               .isEmpty())
                                                               .collect(CollectorUtils.toSortedSet());

                        Map<String, Protein> nameToProtein = recptorNames.stream()
                                                                         .peek(name -> LOG.info("Resolving receptor: " + name))
                                                                         .map(name -> nameToFamily.get(name))
                                                                         .map(family -> restAccessor.getProtein(family.getSlug()))
                                                                         .flatMap(proteins -> proteins.stream())
                                                                         //                                                                         .peek(protein -> LOG.info("Species: " + protein.getSpecies()))
                                                                         .filter(protein -> StringUtils.equalsIgnoreCase(species, protein.getSpecies()))
                                                                         .collect(Collectors.toMap(p -> p.getName(), p -> p));
                        return nameToProtein;
                    }

                    private String normalizeName(String name)
                    {
                        return StringEscapeUtils.unescapeHtml4(name)
                                                .replaceAll("\\<[^\\>]+\\>", "");
                    }

                    private Receptor createReceptor(String name, Protein protein)
                    {
                        return new Receptor()
                        {
                            @Override
                            public String getName()
                            {
                                return name;
                            }

                            @Override
                            public String getNameHtmlEncoded()
                            {
                                return protein.getName();
                            }

                            @Override
                            public String getUniprotId()
                            {
                                return protein.getAccession();
                            }

                            @Override
                            public Set<String> getGenes()
                            {
                                return protein.getGenes();
                            }

                        };
                    }

                };
            }

            @Override
            public SpeciesGPCRDBDataAccessor forHuman()
            {
                return this.forSpecies("Homo sapiens");
            }

            @Override
            public GPCRDBDataAccessor usingLocalCache()
            {
                this.restAccessor.usingLocalCache();
                return this;
            }

            @Override
            public GPCRDBDataAccessor usingCache(Cache cache)
            {
                this.restAccessor.usingCache(cache);
                return this;
            }
        };

    }
}
