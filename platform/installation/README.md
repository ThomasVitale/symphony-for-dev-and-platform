# Getting Started with the Kadras Engineering Platform

This guide describes how to install the Kadras Engineering Platform on a local Kubernetes cluster and on a cloud cluster. It will also cover how to deploy a sample application workload that will take advantage of the platform capabilities such as serverless runtime, ingress and certificate management, and GitOps.

## Cloud Installation

### 0. Before you begin

To follow the guide, ensure you have the following tools installed in your local environment:

* Kubernetes [`kubectl`](https://kubectl.docs.kubernetes.io/installation/kubectl)
* Carvel [`kctrl`](https://carvel.dev/kapp-controller/docs/latest/install)
* Carvel [`kapp`](https://carvel.dev/kapp-controller/docs/latest/install/#installing-kapp-controller-cli-kctrl).

### 1. Deploy Carvel kapp-controller

The platform relies on the Kubernetes-native package management capabilities offered by Carvel [kapp-controller](https://carvel.dev/kapp-controller). You can install it with Carvel [`kapp`](https://carvel.dev/kapp/docs/latest/install) (recommended choice) or `kubectl`.

```shell
kapp deploy -a kapp-controller -y \
  -f https://github.com/carvel-dev/kapp-controller/releases/latest/download/release.yml
```

### 2. Add the Kadras Package Repository

Add the Kadras repository to make the platform packages available to the cluster.

  ```shell
  kctrl package repository add -r kadras-packages \
    --url ghcr.io/kadras-io/kadras-packages:0.17.3 \
    -n kadras-system --create-namespace
  ```

### 3. Create the Secrets for the Developer Portal

The platform includes a developer portal based on Backstage, which is integrated with GitHub out-of-the-box. To make the integration work, you need to generate a Personal Access Token (PAT) from your GitHub account. Furthermore, you need to create an OAuth App on GitHub and get a pair of client ID and client secret. Follow the [official documentation](https://backstage.io/docs/auth/github/provider#create-an-oauth-app-on-github) for creating the OAuth App.

```shell script
export GITHUB_TOKEN=<github-token>
export GITHUB_AUTH_PROVIDER_CLIENT_ID=<github-app-client-id>
export GITHUB_AUTH_PROVIDER_CLIENT_SECRET=<github-app-client-secret>
```

Then, store the credentials in a dedicated Secret on the cluster.

```shell script
kubectl create namespace backstage
kubectl create secret generic developer-portal-secrets \
  --from-literal=GITHUB_TOKEN="${GITHUB_TOKEN}" \
  --from-literal=GITHUB_AUTH_PROVIDER_CLIENT_ID="${GITHUB_AUTH_PROVIDER_CLIENT_ID}" \
  --from-literal=GITHUB_AUTH_PROVIDER_CLIENT_SECRET="${GITHUB_AUTH_PROVIDER_CLIENT_SECRET}" \
  --namespace=backstage
```

### 4. Configure the Platform

The installation of the Kadras Engineering Platform can be configured via YAML. A `values-test.yml` file is provided in the current folder with configuration to customize the cloud installation of the platform, based on the `run` installation profile. Make sure to update the domain names included in the YAML file with one of yours.

### 5. Install the Platform

Reference the `values-test.yml` file mentioned in the previous step and install the Kadras Engineering Platform.

  ```shell
  kctrl package install -i engineering-platform \
    -p engineering-platform.packages.kadras.io \
    -v 0.15.3 \
    -n kadras-system \
    --values-file values-staging.yml
  ```

### 6. Verify the Installation

Verify that all the platform components have been installed and properly reconciled.

  ```shell
  kctrl package installed list -n kadras-system
  ```

### 7. Configure OpenAI

If you want to run LLM-powered applications on the platform and integrate with OpenAI, you need to configure your own API Key.

```shell script
export OPENAI_API_KEY=<openai-api-key>
```

Then, store the API key in a dedicated Secret on the cluster.

```shell script
kubectl create secret generic openai-secret \
  --from-literal=api-key="${OPENAI_API_KEY}" \
  --namespace=kadras-system
```

### 8. Accessing Grafana

If you want to access Grafana, you can get the credentials from the dedicated Secret on the cluster.

```shell script
echo "Admin Username: $(kubectl get secret --namespace observability-stack loki-stack-grafana -o jsonpath="{.data.admin-user}" | base64 --decode)"
echo "Admin Password: $(kubectl get secret --namespace observability-stack loki-stack-grafana -o jsonpath="{.data.admin-password}" | base64 --decode)"
```

## Local Installation

### 0. Before you begin

To follow the guide, ensure you have the following tools installed in your local environment:

* Kubernetes [`kubectl`](https://kubectl.docs.kubernetes.io/installation/kubectl)
* Carvel [`kctrl`](https://carvel.dev/kapp-controller/docs/latest/install)
* Carvel [`kapp`](https://carvel.dev/kapp-controller/docs/latest/install/#installing-kapp-controller-cli-kctrl).

### 1. Create a local Kubernetes cluster

Create a local Kubernetes cluster with [kind](https://kind.sigs.k8s.io).

```shell
cat <<EOF | kind create cluster --config=-
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
name: kadras
nodes:
- role: control-plane
  extraPortMappings:
  - containerPort: 80
    hostPort: 80
    protocol: TCP
  - containerPort: 443
    hostPort: 443
    protocol: TCP
EOF
```

### 2. Deploy Carvel kapp-controller

The platform relies on the Kubernetes-native package management capabilities offered by Carvel [kapp-controller](https://carvel.dev/kapp-controller). You can install it with Carvel [`kapp`](https://carvel.dev/kapp/docs/latest/install) (recommended choice) or `kubectl`.

```shell
kapp deploy -a kapp-controller -y \
  -f https://github.com/carvel-dev/kapp-controller/releases/latest/download/release.yml
```

### 3. Add the Kadras Package Repository

Add the Kadras repository to make the platform packages available to the cluster.

  ```shell
  kctrl package repository add -r kadras-packages \
    --url ghcr.io/kadras-io/kadras-packages:0.17.3 \
    -n kadras-system --create-namespace
  ```

### 4. Create the Secrets for the Developer Portal

The platform includes a developer portal based on Backstage, which is integrated with GitHub out-of-the-box. To make the integration work, you need to generate a Personal Access Token (PAT) from your GitHub account. Furthermore, you need to create an OAuth App on GitHub and get a pair of client ID and client secret. Follow the [official documentation](https://backstage.io/docs/auth/github/provider#create-an-oauth-app-on-github) for creating the OAuth App.

```shell script
export GITHUB_TOKEN=<github-token>
export GITHUB_AUTH_PROVIDER_CLIENT_ID=<github-app-client-id>
export GITHUB_AUTH_PROVIDER_CLIENT_SECRET=<github-app-client-secret>
```

Then, store the credentials in a dedicated Secret on the cluster.

```shell script
kubectl create namespace backstage
kubectl create secret generic developer-portal-secrets \
  --from-literal=GITHUB_TOKEN="${GITHUB_TOKEN}" \
  --from-literal=GITHUB_AUTH_PROVIDER_CLIENT_ID="${GITHUB_AUTH_PROVIDER_CLIENT_ID}" \
  --from-literal=GITHUB_AUTH_PROVIDER_CLIENT_SECRET="${GITHUB_AUTH_PROVIDER_CLIENT_SECRET}" \
  --namespace=backstage
```

### 5. Configure the Platform

The installation of the Kadras Engineering Platform can be configured via YAML. A `values-local.yml` file is provided in the current folder with configuration to customize the local installation of the platform, based on the `run` installation profile.

The Ingress is configured with the special domain `127.0.0.1.sslip.io` which will resolve to your localhost and be accessible via the local cluster.

### 6. Install the Platform

Reference the `values-local.yml` file mentioned in the previous step and install the Kadras Engineering Platform.

  ```shell
  kctrl package install -i engineering-platform \
    -p engineering-platform.packages.kadras.io \
    -v 0.15.3 \
    -n kadras-system \
    --values-file values-local.yml
  ```

### 7. Verify the Installation

Verify that all the platform components have been installed and properly reconciled.

  ```shell
  kctrl package installed list -n kadras-system
  ``` 

### 8. Configure OpenAI

If you want to run LLM-powered applications on the platform and integrate with OpenAI, you need to configure your own API Key.

```shell script
export OPENAI_API_KEY=<openai-api-key>
```

Then, store the API key in a dedicated Secret on the cluster.

```shell script
kubectl create secret generic openai-secret \
  --from-literal=api-key="${OPENAI_API_KEY}" \
  --namespace=kadras-system
```

### 9. Accessing Grafana

If you want to access Grafana, you can get the credentials from the dedicated Secret on the cluster.

```shell script
echo "Admin Username: $(kubectl get secret --namespace observability-stack loki-stack-grafana -o jsonpath="{.data.admin-user}" | base64 --decode)"
echo "Admin Password: $(kubectl get secret --namespace observability-stack loki-stack-grafana -o jsonpath="{.data.admin-password}" | base64 --decode)"
```

## Run an Application via CLI

Kadras Engineering Platform provides capabilities to support application deployment workflows from image to URL based on Knative or plain Kubernetes. Furthermore, you can optionally use the built-in GitOps capabilities provided by Flux or Carvel.

For this example, let's use the [kn](https://knative.dev/docs/client) CLI to deploy an application workload in a serverless runtime provided by Knative.

```shell
kn service create band-service \
  --image ghcr.io/thomasvitale/band-service \
  --security-context strict
```

If you're running the platform in the cloud, the application will be available through a public URL with a valid certificate issued by Let's Encrypt (via Contour and cert-manager) and autoscaling capabilities (thanks to Knative). You can open the URL in the browser or use a CLI like [httpie](https://httpie.io).

```shell
https band-service.default.<your-domain>
```

If you're running the platform locally, the application will be available through a local URL with a self-signed certificate (via Contour and cert-manager) and autoscaling capabilities (thanks to Knative). You can open the URL in the browser or use a CLI like [httpie](https://httpie.io).

```shell
https band-service.default.127.0.0.1.sslip.io --verify no
```

After testing the application, remember to delete it.

```shell
kn service delete band-service
```

## Run an Application via GitOps

Let's now deploy the same application using a GitOps workflow powered by Flux. You can either apply the Flux resources directly to the cluster or use the convenient [Flux CLI](https://fluxcd.io/flux/installation/#install-the-flux-cli). We'll use the second approach.

First, configure a Git repository for Flux to monitor.

```shell
flux create source git band-service \
  --url=https://github.com/ThomasVitale/band-service \
  --branch=main \
  --interval=1m
```

Then, create a Flux Kustomization that will deploy the application.

```shell
flux create kustomization band-service \
  --target-namespace=default \
  --source=band-service \
  --path="./k8s" \
  --wait=true
```

If you're running the platform in the cloud, the application will be available through a public URL with a valid certificate issued by Let's Encrypt (via Contour and cert-manager) and autoscaling capabilities (thanks to Knative). You can open the URL in the browser or use a CLI like [httpie](https://httpie.io).

```shell
https band-service.default.<your-domain>
```

If you're running the platform locally, the application will be available through a local URL with a self-signed certificate (via Contour and cert-manager) and autoscaling capabilities (thanks to Knative). You can open the URL in the browser or use a CLI like [httpie](https://httpie.io).

```shell
https band-service.default.127.0.0.1.sslip.io --verify no
```
