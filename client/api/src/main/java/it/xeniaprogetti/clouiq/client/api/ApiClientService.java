package it.xeniaprogetti.clouiq.client.api;

import java.util.List;

import it.xeniaprogetti.clouiq.client.api.model.Cluster;
import it.xeniaprogetti.clouiq.client.api.model.Host;
import it.xeniaprogetti.clouiq.client.api.model.VM;

public interface ApiClientService {
    /**
     * Get the VMS.
     *
     * @return a list of {@link VM}s
     * @throws CloidIQException "see message for detail"
     */
    List<VM> getVMS() throws CloidIQException;

    /**
     * Get the VM by uuid.
     *
     * @param  uuid the uuid of the VM
     * @return a {@link VM}s
     * @throws CloidIQException "see message for detail"
     */
    VM getVM(String uuid) throws CloidIQException;

    /**
     * Get the Hosts.
     *
     * @return a list of {@link Host}s
     * @throws CloidIQException "see message for detail"
     */
    List<Host> getHosts() throws CloidIQException;

    /**
     * Get the Host by uuid.
     *
     * @param  uuid the uuid of the VM
     * @return a {@link Host}
     * @throws CloidIQException "see message for detail"
     */
    Host getHost(String uuid) throws CloidIQException;

    /**
     * Get the Clusters.
     *
     * @return a list of {@link Cluster}s
     * @throws CloidIQException "see message for detail"
     */
    List<Cluster> getClusters() throws CloidIQException;

    /**
     * Get the Cluster by uuid.
     *
     * @param  uuid the uuid of the VM
     * @return a {@link Cluster}s
     * @throws CloidIQException "see message for detail"
     */
    Cluster getCluster(String uuid) throws CloidIQException;

}
