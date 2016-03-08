package org.ovirt.engine.ui.uicommonweb.models.hosts;

import java.util.Map;

import org.ovirt.engine.core.common.businessentities.network.HostNetworkQos;
import org.ovirt.engine.core.common.businessentities.network.IPv4Address;
import org.ovirt.engine.core.common.businessentities.network.IpV6Address;
import org.ovirt.engine.core.common.businessentities.network.Ipv4BootProtocol;
import org.ovirt.engine.core.common.businessentities.network.Ipv6BootProtocol;
import org.ovirt.engine.core.common.businessentities.network.NetworkAttachment;
import org.ovirt.engine.core.common.businessentities.network.VdsNetworkInterface;

public interface InterfacePropertiesAccessor {
    String getIpv4Address();

    String getIpv4Netmask();

    String getIpv4Gateway();

    Ipv4BootProtocol getIpv4BootProtocol();

    String getIpv6Address();

    Integer getIpv6Prefix();

    String getIpv6Gateway();

    Ipv6BootProtocol getIpv6BootProtocol();

    boolean isQosOverridden();

    HostNetworkQos getHostNetworkQos();

    Map<String, String> getCustomProperties();

    class FromNic implements InterfacePropertiesAccessor {
        VdsNetworkInterface nic;

        public FromNic(VdsNetworkInterface nic) {
            this.nic = nic;
        }

        @Override
        public String getIpv4Address() {
            return nic.getIpv4Address();
        }

        @Override
        public String getIpv4Netmask() {
            return nic.getIpv4Subnet();
        }

        @Override
        public String getIpv4Gateway() {
            return nic.getIpv4Gateway();
        }

        @Override
        public Ipv4BootProtocol getIpv4BootProtocol() {
            return nic.getIpv4BootProtocol();
        }

        @Override
        public String getIpv6Address() {
            return nic.getIpv6Address();
        }

        @Override
        public Integer getIpv6Prefix() {
            return nic.getIpv6Prefix();
        }

        @Override
        public String getIpv6Gateway() {
            return nic.getIpv6Gateway();
        }

        @Override
        public Ipv6BootProtocol getIpv6BootProtocol() {
            return nic.getIpv6BootProtocol();
        }

        @Override
        public HostNetworkQos getHostNetworkQos() {
            return nic.getQos();
        }

        @Override
        public boolean isQosOverridden() {
            return false;
        }

        @Override
        public Map<String, String> getCustomProperties() {
            return null;
        }
    }

    class FromNetworkAttachment implements InterfacePropertiesAccessor {
        NetworkAttachment networkAttachment;
        IPv4Address iPv4Address;
        IpV6Address iPv6Address;
        HostNetworkQos networkQos;

        public FromNetworkAttachment(NetworkAttachment networkAttachment, HostNetworkQos networkQos) {
            this.networkAttachment = networkAttachment;
            this.iPv4Address = networkAttachment.getIpConfiguration() != null
                    && networkAttachment.getIpConfiguration().hasIpv4PrimaryAddressSet()
                            ? networkAttachment.getIpConfiguration().getIpv4PrimaryAddress()
                            : null;
            this.iPv6Address = networkAttachment.getIpConfiguration() != null
                    && networkAttachment.getIpConfiguration().hasIpv6PrimaryAddressSet()
                            ? networkAttachment.getIpConfiguration().getIpv6PrimaryAddress()
                            : null;
            this.networkQos = networkQos;
        }

        @Override
        public String getIpv4Address() {
            return iPv4Address.getAddress();
        }

        @Override
        public String getIpv4Netmask() {
            return iPv4Address.getNetmask();
        }

        @Override
        public String getIpv4Gateway() {
            return iPv4Address.getGateway();
        }

        @Override
        public Ipv4BootProtocol getIpv4BootProtocol() {
            return iPv4Address.getBootProtocol();
        }

        @Override
        public String getIpv6Address() {
            return iPv6Address.getAddress();
        }

        @Override
        public Integer getIpv6Prefix() {
            return iPv6Address.getPrefix();
        }

        @Override
        public String getIpv6Gateway() {
            return iPv6Address.getGateway();
        }

        @Override
        public Ipv6BootProtocol getIpv6BootProtocol() {
            return iPv6Address.getBootProtocol();
        }

        @Override
        public HostNetworkQos getHostNetworkQos() {
            if (networkAttachment.isQosOverridden()) {
                return networkAttachment.getHostNetworkQos();
            } else {
                return networkQos;
            }
        }

        @Override
        public boolean isQosOverridden() {
            return networkAttachment.isQosOverridden();
        }

        @Override
        public Map<String, String> getCustomProperties() {
            return networkAttachment.getProperties();
        }
    }
}
