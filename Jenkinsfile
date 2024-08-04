pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('mingy1206-dockerhub')
        dockerImage = ''
    }
    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/develop']], extensions: [], userRemoteConfigs: [[credentialsId: 'mingy1206-github', url: "${GITHUB_ADDRESS_BE}"]])
            }
        }
        stage('Build App') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build -x test'
            }
			post {
				success {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키잖아!",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Build Success",
					webhookURL: "$DISCORD"
				}
			}
				failure {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키니시티잖아...",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Build Failed",
					webhookURL: "$DISCORD"
					}
				}
			}
        }
        stage('Test App') {
            steps {
                sh './gradlew test'
            }
			post {
				success {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키잖아!",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Test Success",
					webhookURL: "$DISCORD"
				}
			}
				failure {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키니시티잖아...",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Test Failed",
					webhookURL: "$DISCORD"
					}
				}
			}
        }
		stage('Login Docker') {
            steps {
                sh "docker login ${CR_ADDRESS} --username ${ACCESS_ID} --password ${ACCESS_PW}"
            }
        }
        stage('Build Image') {
            steps {
                script {
                    sh "docker build -t ${BACKEND_CR_ADDRESS}backend:latest ."
					sh "docker build -t ${BACKEND_CR_ADDRESS}backend:${BUILD_NUMBER} ."
                }
            }
			post {
				success {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키잖아!",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Build Success",
					webhookURL: "$DISCORD"
				}
			}
				failure {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키니시티잖아...",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Build Failed",
					webhookURL: "$DISCORD"
					}
				}
			}
        }
        stage('Push & Clean Image') {
            steps {
                script {
                    sh "docker push ${BACKEND_CR_ADDRESS}backend:latest"
                    sh "docker push ${BACKEND_CR_ADDRESS}backend:${BUILD_NUMBER}"


                    sh "docker rmi ${BACKEND_CR_ADDRESS}backend:latest"
                    sh "docker rmi ${BACKEND_CR_ADDRESS}backend:${BUILD_NUMBER}"

                }
            }
			post {
				success {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키잖아!",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Push Success",
					webhookURL: "$DISCORD"
				}
			}
				failure {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키니시티잖아...",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Push Failed",
					webhookURL: "$DISCORD"
					}
				}
			}
        }
        stage('Canary Deployment to Instance A') {
            steps {
                script {
                    sshagent(['kc-enter-be-a']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ubuntu@${KC_ENTER_BE_A_IP} '
							sudo docker login ${CR_ADDRESS} --username ${ACCESS_ID} --password ${ACCESS_PW} &&
                            sudo docker pull ${BACKEND_CR_ADDRESS}backend:latest &&
                            sudo docker stop app || true &&
                            sudo docker rm app || true &&
                            sudo docker run -d -e JAVA_OPTS1=-Ddev-vault-token=${VAULT_TOKEN} -e JAVA_OPTS2=-Ddev-vault-host=${VAULT_HOST} -e JAVA_OPTS3=-Ddev-vault-port=${VAULT_PORT} --name app -p ${BACKEND_APP_PORT}:${BACKEND_APP_PORT} ${BACKEND_CR_ADDRESS}backend:latest
                            '
                        """
                    }
                }
            }
			post {
				success {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키잖아!",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Deploy 1/2 Success -> Next Stage 3~9M Wait",
					webhookURL: "$DISCORD"
				}
			}
				failure {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키니시티잖아...",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Deploy 1/2 Failed",
					webhookURL: "$DISCORD"
					}
				}
			}
        }
        stage('Health Check') {
            steps {
				script {
					def retries = 3
					def delay = 180 // seconds
					def success = false
					for (int i = 0; i < retries; i++) {
						def canaryHealth = sh(script: "curl -s http://${KC_ENTER_BE_A_IP}:${BACKEND_APP_PORT}/actuator/health", returnStatus: true)
						if (canaryHealth == 0) {
							success = true
							break
						}
						sleep(delay)
					}
					if (!success) {
						error("Canary deployment failed. Instance A health check failed after ${retries} retries.")
					}
                }
            }
			post {
				success {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키잖아!",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Deploy 1/2 State Check Success",
					webhookURL: "$DISCORD"
				}
			}
				failure {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키니시티잖아...",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Deploy 1/2 State Check Failed",
					webhookURL: "$DISCORD"
					}
				}
			}
        }
        stage('Canary Deployment to other Instance') {
            steps {
                script {
                    // input message: 'Proceed with full deployment?', ok: 'Deploy '

                    sshagent(['kc-enter-be-b']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ubuntu@${KC_ENTER_BE_B_IP} '
							sudo docker login ${CR_ADDRESS} --username ${ACCESS_ID} --password ${ACCESS_PW} &&
                            sudo docker pull ${BACKEND_CR_ADDRESS}backend:latest &&
                            sudo docker stop app || true &&
                            sudo docker rm app || true &&
                            sudo docker run -d -e JAVA_OPTS1=-Ddev-vault-token=${VAULT_TOKEN} -e JAVA_OPTS2=-Ddev-vault-host=${VAULT_HOST} -e JAVA_OPTS3=-Ddev-vault-port=${VAULT_PORT} --name app -p ${BACKEND_APP_PORT}:${BACKEND_APP_PORT} ${BACKEND_CR_ADDRESS}backend:latest
                            '
                        """
                    }
                }
            }
			post {
				success {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키잖아!",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Deploy 2/2 Success",
					webhookURL: "$DISCORD"
				}
			}
				failure {
					withCredentials([string(credentialsId: 'jenkins-be-webhook', variable: 'DISCORD')]) {
					discordSend description: "Jenkins CI/CD Trigger Alarm",
					footer: "이거 완전 럭키비키니시티잖아...",
					link: env.BUILD_URL, result: currentBuild.currentResult,
					title: "Jenkins CI/CD Image Deploy 2/2 Failed",
					webhookURL: "$DISCORD"
					}
				}
			}
        }
    }
}




