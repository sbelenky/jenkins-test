final String DEFAULT_TEST_WORKSPACE_DIR = './test-workspace'
final String PROJECT = 'agg-updater'

def cli = new CliBuilder(usage:'jenkins-test')

cli.awsAccessKeyId(required:true, args:1, 'AWS Access Key Id')
cli.awsSecretAccessKey(required:true, args:1, 'AWS Secret Access Key')
cli.awsKeypairName(required:true, args:1, 'AWS Keypair Name')
cli.awsPrivateKeyPath(required:true, args:1, 'AWS Private Key Path')

cli.testWorkspaceDir(required:false, args:1, "Workspace Directory for Testing, default is $DEFAULT_TEST_WORKSPACE_DIR") 

def options = cli.parse(args)

if (!options) {
	System.exit(1)
}

File testWorkspace(String... subDirs) {
	File rtrn = new File(options.testWorkspaceDir ? options.testWorkspaceDir : DEFAULT_TEST_WORKSPACE_DIR)
	subDirs.each { rtrn = new File(rtrn, it) }
	return rtrn
}

println "Begin running test"

def testWorkspaceDir = new File(options.testWorkspaceDir ? options.testWorkspaceDir : DEFAULT_TEST_WORKSPACE_DIR)
testWorkspaceDir.exists() && testWorkspaceDir.deleteDir()
testWorkspaceDir.mkdir()

"git clone https://github.com/sbelenky/${PROJECT}.git".execute(null, testWorkspaceDir)
"mvn install".execute(null, new File(testWorkspaceDir, PROJECT))
//Files.copy(new File(testWorkspaceDir, PROJECT), 'target/)
