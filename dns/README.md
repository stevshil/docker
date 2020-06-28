# Docker DNS container

This project allows you to run your own DNS container within your own server.

The container can be mapped to your own /etc/named.conf and your own /var/named directory so that you can make changes to the DNS data without having to rebuild the image.

Source for the image can be found at https://github.com/stevshil/docker.git in the **dns** directory.

## Running the container

### Starting
```
$ docker run -itd --name mydns -p53:53/tcp -p53:53/udp -v/home/mydns/etc:/etc/mydns -v/home/mydns/var/named:/var/mydns --dns=127.0.0.1 steve353/dns
```

### After changing local data files

```
$ docker restart mydns
```

### Key points

* The requirement of port 53 for both tcp and udp.  If this is not done then your host won't respond to the DNS requests.  
* If you are running firewalld or iptables ensure **dns** is allowed.
* The ```--dns``` is required to ensure that the container itself is using its own DNS set up.

## Set up

You will need to create your own configuration directory to be able to set your IP address and DNS data.

### Example

```
$ mkdir -p /home/mydns/etc /home/mydns/var/named
$ cd ~/mydns/etc
$ wget https://github.com/stevshil/docker/raw/9222b700920907827032b9654537b6b4ccd5695b/dns/files/etc/named.conf
```

**Edit the file named.conf and change the following;**

```zone "training.local" IN {```

Change the *training.local* to the domain name you wish to use.

```zone "10.168.192.in-addr.arpa" IN {```

Change the *10.168.192* to the reverse IP address of your network that you will be providing DNS for.

```
$ cd ../var/named
$ wget https://github.com/stevshil/docker/raw/9222b700920907827032b9654537b6b4ccd5695b/dns/files/var_named/forward.data
$ wget https://github.com/stevshil/docker/raw/9222b700920907827032b9654537b6b4ccd5695b/dns/files/var_named/reverse.data
```

**Edit the files**

These are the DNS data files that will need to contain the names of your servers and their IP addresses.

* forward.data
  - This file contains the name to IP address resolution
  - If you changed the name of your domain in **named.conf** you will need to change the domain name at the top of the file, e.g. replace *training.local* with *yourdomain.suffix*
    - Also change the **IN NS** line so that **ns1.training.local** becomes **ns1.yourdomain.suffix**
  - Change the IPs and other names as you require
* reverse.data
  - This is the IP to hostname reverse lookup
  - Change the domain names if you have done so in **named.conf** to match in this file
  - The IP addresses and names for the data will also need changing
