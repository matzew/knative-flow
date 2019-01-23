FROM registry.fedoraproject.org/fedora-minimal
MAINTAINER Matthias Wessendorf <matzew@apache.org>
ARG BINARY=./data-sink

COPY ${BINARY} /opt/data-sink
ENTRYPOINT ["/opt/data-sink"]
