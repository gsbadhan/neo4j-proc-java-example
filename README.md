## download Neo4j:
wget http://dist.neo4j.org/neo4j-enterprise-3.2.5-unix.tar.gz


## Neo4j Browser URL:
http://localhost:7474/


##Basic Neo4j commands:
 1. connect to cyher-shell: ./cypher-shell -u neo4j -p test
 
 
## How deploy PROC jar:
	1. run : mvn clean install
	2. copy "neo4j-procedure-example-0.1.0.jar" from /target folder. And paste to Neo4j's ~/plugins/ folder.
Note: Stop Neo4j server before deploying PROC.

## neo4j.propperties changes:
	set : dbms.security.procedures.unrestricted=apoc.*,org.digi.*
	