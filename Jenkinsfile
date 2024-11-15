pipeline {
    agent {
        kubernetes {
            label 'eks-agent'
            defaultContainer 'jnlp'
        }
    }
    parameters {
        string(name: 'TAG_NAME', defaultValue: 'your-default-tag-name', description: 'Tag name to be added to Harbor')
        string(name: 'IMAGE_TAG', defaultValue: 'latest', description: 'New image tag version')
    }
    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://your-git-repo-url.git', branch: 'main'
            }
        }
        stage('Add Tag to Harbor') {
            steps {
                script {
                    def harborUrl = 'https://your-harbor-url/api/repositories/your-repo/tags'
                    def tagName = params.TAG_NAME // Use the parameter for tag name
                    def auth = 'your-username:your-password' // Use credentials for security

                    sh """
                    curl -X POST -u ${auth} -H "Content-Type: application/json" \
                    -d '{"name": "${tagName}"}' ${harborUrl}
                    """
                }
            }
        }
        stage('Update Image Tag in values.yaml') {
            steps {
                script {
                    def newImageTag = params.IMAGE_TAG // Get the new image tag from the parameter
                    def valuesFilePath = 'path/to/your/values.yaml' // Specify the path to your values.yaml file

                    // Update the image tag in values.yaml
                    sh """
                    sed -i 's/tag: .*/tag: ${newImageTag}/' ${valuesFilePath}
                    """
                }
            }
        }
        stage('Update Git Repo File') {
            steps {
                script {
                    // Update your file here
                    writeFile file: 'path/to/your/file.txt', text: 'Updated content with tag: ${tagName}'
                    sh 'git config user.email "you@example.com"'
                    sh 'git config user.name "Your Name"'
                    sh 'git add path/to/your/file.txt'
                    sh 'git add path/to/your/values.yaml' // Add the modified values.yaml
                    sh 'git commit -m "Updated image tag to ${params.IMAGE_TAG} and file with new content"'
                    sh 'git push origin main'
                }
            }
        }
    }
}