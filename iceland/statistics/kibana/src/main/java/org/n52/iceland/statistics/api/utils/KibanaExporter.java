/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.statistics.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.util.Arrays;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.n52.iceland.statistics.api.utils.dto.KibanaConfigEntryDto;
import org.n52.iceland.statistics.api.utils.dto.KibanaConfigHolderDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class KibanaExporter {

    private static String statisticsIndex;

    private static RestHighLevelClient client;

    // CHECKSTYLE:OFF
    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.out.printf("Usage: java KibanaExporter.jar %s %s%n", "localhost:9300", "my-cluster-name");
            System.exit(0);
        }
        if (!args[0].contains(":")) {
            throw new IllegalArgumentException(
                    String.format("%s not a valid format. Expected <hostname>:<port>.", args[0]));
        }

        // set ES address
        String split[] = args[0].split(":");

        // set cluster name
        Builder tcSettings = Settings.builder();
        tcSettings.put("cluster.name", args[1]);
        System.out.println("Connection to " + args[1]);

        client = new RestHighLevelClient(RestClient
                .builder(new HttpHost(InetAddress.getByName(split[0]), Integer.parseInt(split[1], 10), "http")));

        // search index pattern for needle
        searchIndexPattern();

        KibanaConfigHolderDto holder = new KibanaConfigHolderDto();
        System.out.println("Reading .kibana index");

        SearchResponse resp = client.search(new SearchRequest(".kibana").source(new SearchSourceBuilder().size(1000)),
                RequestOptions.DEFAULT);
        Arrays.asList(resp.getHits().getHits()).stream().map(KibanaExporter::parseSearchHit).forEach(holder::add);
        System.out.println("Reading finished");

        ObjectMapper mapper = new ObjectMapper();
        // we love pretty things
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        File f = new File("kibana_config.json");

        try (FileOutputStream out = new FileOutputStream(f, false)) {
            mapper.writeValue(out, holder);
        }

        System.out.println("File outputted to: " + f.getAbsolutePath());

        client.close();

    }
    // CHECKSTYLE:ON

    private static void searchIndexPattern() throws Exception {
        // find statistics index
        System.out.println("Searching index pattern name for index-needle");
        SearchResponse indexPatternResp =
                client.search(new SearchRequest(".kibana").types("index-pattern"), RequestOptions.DEFAULT);

        if (indexPatternResp.getHits().getHits().length != 1) {
            throw new Exception("The .kibana/index-pattern type has multiple elements or none. "
                    + "Only one element is legal. Set your kibana settings with only one index-pattern");
        }

        statisticsIndex = indexPatternResp.getHits().getHits()[0].getId();
        System.out.println("Found index " + statisticsIndex);
    }

    private static KibanaConfigEntryDto parseSearchHit(SearchHit hit) {
        System.out.printf("Reading %s/%s/%s%n", hit.getIndex(), hit.getType(), hit.getId());

        String id = hit.getId();
        if (hit.getId().equals(statisticsIndex)) {
            id = KibanaImporter.INDEX_NEEDLE;
        }

        String source = hit.getSourceAsString().replace(statisticsIndex, KibanaImporter.INDEX_NEEDLE);

        return new KibanaConfigEntryDto(hit.getIndex(), hit.getType(), id, source);

    }
}
