properties([
    parameters([
        booleanParam (
            name: "ENABLE_SCAN",
            description: "to enable Jenkins Nexus and Checkmarx Scans",
            defaultValue: "false"
        )
    ]),
    pipelineTriggers([])
])

env.CURRENT_BRANCH_NAME = ((env.BRANCH_NAME.substring(env.BRANCH_NAME.lastIndexOf("/") + 1)).replaceAll('-', '_'))


if (env.BRANCH_NAME.toLowerCase().startsWith('release')) {
    echo "[INFO] - Using artifactory cybs-yum repo for Release branch"
    env.YUM_HOSTNAME = "https://artifactory.trusted.visa.com/cybs-yum"
}


timestamps {
    timeout(time: var.timeoutTime, unit: var.timeoutUnit) {
        catchError {
            parallel(
                CHECKMARX1: {
                    node('pyslave') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for Auth java"
                                sh "echo -e \"\nRunning checkmarx\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSJAVAAUTHSDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_java,checkmarx_php,checkmarx_ruby,dotnet_checkmarx,node_checkmarx,python_checkmarx,checkmarx_dotnetStandard',
                                        ignoreBranchType: true
                                    )
                                }
                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                },
                CHECKMARX2: {
                    node('pyslave') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for Java Client SDK"
                                sh "echo -e \"\nRunning checkmarx for Java Client SDK\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSJAVASDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_javaAuth,checkmarx_php,checkmarx_ruby,dotnet_checkmarx,node_checkmarx,python_checkmarx,checkmarx_dotnetStandard',
                                        ignoreBranchType: true
                                    )
                                }

                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                },
                CHECKMARX3: {
                    node('pyslave') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for PHP client SDK"
                                sh "echo -e \"\nRunning checkmarx for PHP client SDK\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSPHPSDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_java,checkmarx_javaAuth,checkmarx_ruby,dotnet_checkmarx,node_checkmarx,python_checkmarx,checkmarx_dotnetStandard',
                                        ignoreBranchType: true
                                    )
                                }

                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                },
                CHECKMARX4: {
                    node('pyslave') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for REST Client Ruby"
                                sh "echo -e \"\nRunning checkmarx for REST Client Ruby\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSRUBYSDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_java,checkmarx_javaAuth,checkmarx_php,dotnet_checkmarx,node_checkmarx,python_checkmarx,checkmarx_dotnetStandard',
                                        ignoreBranchType: true
                                    )
                                }

                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                },
                CHECKMARX5: {
                    node('dockerclient') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for REST Client DotNet"
                                sh "echo -e \"\nRunning checkmarx\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSDOTNETSDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_java,checkmarx_javaAuth,checkmarx_php,checkmarx_ruby,node_checkmarx,python_checkmarx,checkmarx_dotnetStandard',
                                        ignoreBranchType: true
                                    )
                                }

                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                },
                CHECKMARX6: {
                    node('dockerclient') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for Rest Client Node"
                                sh "echo -e \"\nRunning checkmarx\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSNODESDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_java,checkmarx_javaAuth,checkmarx_php,checkmarx_ruby,dotnet_checkmarx,python_checkmarx,checkmarx_dotnetStandard',
                                        ignoreBranchType: true
                                    )
                                }

                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                },
                CHECKMARX7: {
                    node('dockerclient') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for Rest Client PYTHON"
                                sh "echo -e \"\nRunning checkmarx\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSPYTHONSDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_java,checkmarx_javaAuth,checkmarx_php,checkmarx_ruby,dotnet_checkmarx,node_checkmarx,checkmarx_dotnetStandard',
                                        ignoreBranchType: true
                                    )
                                }

                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                },
                CHECKMARX8: {
                    node('dockerclient') {
                        stage('checkmarx') {
                            if ((env.BRANCH_NAME == 'develop') || (env.BRANCH_NAME.toLowerCase().startsWith('release'))  || (env.ENABLE_SCAN == true)) {
                                echo "Running checkmarx for Rest Client DotNetStandard"
                                sh "echo -e \"\nRunning checkmarx\" "
                                sh "mkdir Checkmarx"
                                dir("Checkmarx") {
                                    GitCheckout(gitUrl: 'git@github.trusted.visa.com:vgitapps/CAPIC-cybersource-rest-auth-java.git')
                                    runCheckmarxScan(
                                        projectName: 'CYBSDOTNETSTANDARDSDK_18592', 
                                        groupId: '6415f08f-4130-475b-b426-3e025571b260', 
                                        excludeFolders: 'test,spec,docs,generator,checkmarx_java,checkmarx_javaAuth,checkmarx_php,checkmarx_ruby,dotnet_checkmarx,node_checkmarx,python_checkmarx',
                                        ignoreBranchType: true
                                    )
                                }

                            }else{
                                echo "Skipping Checkmarx"
                                sh "echo -e \"\nSkipping Checkmarx\" "
                            }
                        }
                    }
                }

            )
        }

        node('utils') {
            buildFinally()
        }
        
    }
}