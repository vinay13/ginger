cd /tmp
curl --header "X-JFrog-Art-Api: AKCp2WXgcXCL4muLPAcsGyh1yf7D1q5orf1Wf1vJHof5W8SCTbDBoKqWHgogWbY7LqFwrM5SS" https://build.mobigraph.co:8443/artifactory/libs-snapshot/com/mobigraph/ginger-service/ginger-service-1.0.0-SNAPSHOT.zip -o release.zip

unzip /tmp/release.zip

cp ginger-service-1.0.0-SNAPSHOT/lib/ginger-service.ginger-service-1.0.0-SNAPSHOT-sans-externalized.jar /opt/software/gola/prod/lib/
sudo systemctl restart golaweb.service
sudo systemctl status golaweb.service