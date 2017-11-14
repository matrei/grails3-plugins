package com.github.sheehan

import com.agileorbit.schwartz.SchwartzJob
import grails.util.Environment
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

@DisallowConcurrentExecution

@CompileStatic
@Slf4j
class SyncPluginsREADMEJobService implements SchwartzJob {

	PluginService pluginService

	void execute(JobExecutionContext context) throws JobExecutionException {
		log.info 'Fetching plugin README'
		pluginService.refreshReadmeHTML()
		log.info 'plugin READMEupdated'
	}

	void buildTriggers() {
		if (!Environment.developmentMode) {
			triggers << factory('repeat every 12 hour').intervalInHours(12).build()
		}
	}
}
