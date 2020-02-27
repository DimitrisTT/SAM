#!/bash

 mkdir javadocs
 javadoc -d ./javadocs/service/ -sourcepath /src/main/java/ com.tracktik.scheduler.service
 javadoc -d ./javadocs/service/ -sourcepath schedulerservice/src/main/java/ com.tracktik.scheduler.service
 ls javadocs/
 javadoc -d ./javadocs/api/ -sourcepath schedulerapi/src/main/java/ com.tracktik.scheduler.api
 javadoc -d ./javadocs/model/api/domain/ -sourcepath schedulermodel/src/main/java/ com.tracktik.scheduler.model.api.domain
 javadoc -d ./javadocs/model/api/domain/ -sourcepath schedulermodel/src/main/java/ com.tracktik.scheduler.api.domain
 javadoc -d ./javadocs/model/domain/ -sourcepath schedulermodel/src/main/java/ com.tracktik.scheduler.domain
 javadoc -d ./javadocs/model/util/ -sourcepath schedulermodel/src/main/java/ com.tracktik.scheduler.util
 javadoc -d ./javadocs/model/configuration/ -sourcepath schedulermodel/src/main/java/ com.tracktik.scheduler.configuration
 history > temp.txtxt
