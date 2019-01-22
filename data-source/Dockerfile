FROM registry.fedoraproject.org/fedora-minimal
MAINTAINER Matthias Wessendorf <matzew@apache.org>
ARG BINARY=./data-source

COPY ${BINARY} /opt/data-source
ENTRYPOINT ["/opt/data-source"]
