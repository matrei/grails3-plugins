package grailsplugins

import com.agileorbit.schwartz.SchwartzJob
import com.twitter.TwitterService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

@DisallowConcurrentExecution
@CompileStatic
@Slf4j
class SyncPluginsJobService implements SchwartzJob {
	
	GrailsPluginsService grailsPluginsService

	void execute(JobExecutionContext context) throws JobExecutionException {
		log.info 'Fetching latest plugin data'

		grailsPluginsService.refresh()
	}

	void buildTriggers() {
		triggers << factory('repeat every hour').intervalInHours(1).build()
	}
}
