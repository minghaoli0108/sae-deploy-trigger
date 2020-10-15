# deploy-trigger-java
Deploy the trigger to Aliyun Function Compute Service. Each time you push your image tags to ACR Repository, this trigger will deploy it to Aliyun Serverless Application Engine Service.


## build

```bash
git clone git@github.com:AndyManastorm/sae-deploy-trigger.git
cd sae-deploy-trigger/DeployTrgger
mvn clean package

## result path: DeployTrgger/target/deploy-trigger-1.0-SNAPSHOT.jar

```

