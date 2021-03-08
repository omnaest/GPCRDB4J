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

import java.io.File;

import org.omnaest.genomics.gpcrdb.domain.raw.ProteinFamilies;
import org.omnaest.genomics.gpcrdb.domain.raw.Proteins;
import org.omnaest.utils.CacheUtils;
import org.omnaest.utils.cache.Cache;
import org.omnaest.utils.rest.client.RestClient;
import org.omnaest.utils.rest.client.internal.JSONRestClient;

public class GPCRDBRestAPIUtils
{
    private static String baseUrl = "http://gpcrdb.org/services";

    public static interface GPCRRestAccessor
    {

        public ProteinFamilies getProteinFamilies();

        public GPCRRestAccessor usingCache(Cache cache);

        public GPCRRestAccessor usingLocalCache();

        public Proteins getProtein(String slugId);
    }

    public static GPCRRestAccessor getInstance()
    {
        return new GPCRRestAccessor()
        {

            private Cache cache;

            @Override
            public ProteinFamilies getProteinFamilies()
            {
                RestClient restClient = this.createRestClient();

                String url = RestClient.urlBuilder()
                                       .setBaseUrl(baseUrl)
                                       .addPathToken("proteinfamily")
                                       .build();

                return restClient.requestGet(url, ProteinFamilies.class);
            }

            @Override
            public Proteins getProtein(String slugId)
            {
                RestClient restClient = this.createRestClient();
                String url = RestClient.urlBuilder()
                                       .setBaseUrl(baseUrl)
                                       .addPathToken("proteinfamily")
                                       .addPathToken("proteins")
                                       .addPathToken(slugId)
                                       .build();
                return restClient.requestGet(url, Proteins.class);
            }

            private RestClient createRestClient()
            {
                return new JSONRestClient().withCache(this.cache);
            }

            @Override
            public GPCRRestAccessor usingCache(Cache cache)
            {
                this.cache = cache;
                return this;
            }

            @Override
            public GPCRRestAccessor usingLocalCache()
            {
                return this.usingCache(CacheUtils.newJsonFolderCache(new File("cache/GPCRDB")));
            }

        };
    }
}
