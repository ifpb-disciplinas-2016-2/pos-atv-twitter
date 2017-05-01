FROM payara/server-web
MAINTAINER Victor Hugo <victor.hugo.origins@gmail.com>
ENV DOMAIN domain1
ENV LIB /opt/payara41/glassfish/domains/${DOMAIN}/lib/
ENV DEPLOY ${PAYARA_PATH}/glassfish/domains/${DOMAIN}/autodeploy/
ADD target/twitter/WEB-INF/lib/ ${LIB}
ENTRYPOINT $PAYARA_PATH/bin/asadmin start-domain --verbose ${DOMAIN}
ADD target/twitter.war  ${DEPLOY}
