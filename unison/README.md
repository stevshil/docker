# Unison

This project is designed for those like me, who have multiple versions of Linux running due to varied hardware, and need to sync their systems between Debian and RHEL based systems.

This Unison container is designed to be used on a Debian based system that wishes to Sync with RHEL based systems, and is designed for graphical use.

## Building your own version

It is advisable that you make changes to the **Dockerfile** and **docker-compose.yml** files, since you may need to specify different UID and directory for syncing.

This version is prebuilt to work with UID 1000 and GID 1000, and maps **/home** within the container to perform syncing, which you should change in the **Dockerfile**, and then rebuild using;
```
docker-compose build
```
The directory mappings can also be changed in both files if you don't want to expose the whole of your /home.

**NOTE:** The container runs as UID 1000 so after the initial start up and mounting of the **/home** directory as root, the changes will only work with this user.

## Running the project

You should ideally have **docker-compose** installed on your system and use that to launch as follows;

```
docker-compose up
```

## Creating the desktop file

In **/usr/share/applications** you should create a file called **unison.desktop** and add the following;

```
[Desktop Entry]
Name=Unison
GenericName=Unison File Sync
Exec=/usr/bin/unison
Terminal=false
Type=Application
Encoding=UTF-8
Icon=/usr/share/icons/unison-gtk.svg.png
Categories=System;Accessories
Keywords=shell;prompt;command;commandline;cmd;
X-Desktop-File-Install-Version=0.23
```

**NOTE:** The icon file is supplied in this repository.

You will also require a shell script to launch your unison container, and create it in /usr/bin/unison containing the following code;

```
#!/bin/bash

cd $YOURCLONEDLOCATION/unison
docker-compose up
```

Where **$YOURCLONEDLOCATION** is where you cloned this project to.

Then making sure it is executable;
```
chmod +x /usr/bin/unison
```
