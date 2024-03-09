package it.xeniaprogetti.cloudiq.client.v1;

import it.xeniaprogetti.clouiq.client.api.ApiClientCredentials;
import it.xeniaprogetti.clouiq.client.api.ApiClientProvider;
import it.xeniaprogetti.clouiq.client.api.ApiClientService;

public class V1ApiClientProvider implements ApiClientProvider {
    public V1ApiClientProvider() {
    }

    @Override
    public ApiClientService client(ApiClientCredentials credentials) {
        return null;
    }

    @Override
    public boolean validate(ApiClientCredentials credentials) {
        return false;
    }
}
