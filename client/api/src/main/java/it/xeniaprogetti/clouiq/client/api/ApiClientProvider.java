package it.xeniaprogetti.clouiq.client.api;

public interface ApiClientProvider {
    /**
     * Create a client for a Nutanix partner account.
     *
     * @param credentials the credentials to use for the client.
     * @return a NutanixApiClient client
     */
    ApiClientService client(final ApiClientCredentials credentials);

    boolean validate(final ApiClientCredentials credentials);

}
