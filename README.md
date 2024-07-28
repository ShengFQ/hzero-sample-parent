hzero-sample-parent

# hzero-sample-parent
[hzero服务开发文档](https://open.hand-china.com/hzero-docs/v1.3/zh/docs/installation-configuration/service-config/config/)


maven plugin for spring boot

```bigquery
<project>
	<properties>
		<app.profiles>local,dev</app.profiles>
	</properties>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<profiles>${app.profiles}</profiles>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```

### 支持本地环境启动:
$ mvn spring-boot:run -Dspring-boot.run.profiles=dev,local
$ mvn spring-boot:run -Dapp.profiles=test

###打包可执行文件
```
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
			<executions>
				<execution>
					<goals>
						<goal>repackage</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```
