package it.xeniaprogetti.cloudiq.client.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import it.xeniaprogetti.clouiq.client.api.ApiClientService;
import it.xeniaprogetti.clouiq.client.api.CloidIQException;
import it.xeniaprogetti.clouiq.client.api.model.Cluster;
import it.xeniaprogetti.clouiq.client.api.model.Host;
import it.xeniaprogetti.clouiq.client.api.model.VM;


public class V1ApiClientService implements ApiClientService {

    @Override
    public List<VM> getVMS() throws CloidIQException {
        return null;
    }

    @Override
    public VM getVM(String uuid) throws CloidIQException {
        return null;
    }

    @Override
    public List<Host> getHosts() throws CloidIQException {
        return null;
    }

    @Override
    public Host getHost(String uuid) throws CloidIQException {
        return null;
    }

    @Override
    public List<Cluster> getClusters() throws CloidIQException {
        return null;
    }

    @Override
    public Cluster getCluster(String uuid) throws CloidIQException {
        return null;
    }
}
