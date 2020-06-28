FROM centos:7.6.1810

# Update containers packages
yum -y update

# Install uDeploy
RUN mkdir -p /opt/uDeploy
ADD software/UBCD_7.0.0_TRIAL.zip /opt/uDeploy
WORKDIR /opt/uDeploy
COPY properties/* .

