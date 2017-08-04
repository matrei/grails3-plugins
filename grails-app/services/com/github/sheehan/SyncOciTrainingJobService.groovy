package com.github.sheehan

import com.agileorbit.schwartz.SchwartzJob
import com.objectcomputing.training.OciTrainingService
import grails.util.Environment
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

@DisallowConcurrentExecution
@CompileStatic
@Slf4j
class SyncOciTrainingJobService implements SchwartzJob {

	OciTrainingService ociTrainingService

	void execute(JobExecutionContext context) throws JobExecutionException {
		log.info 'Fetching latest training'

		ociTrainingService.refreshTrainingOfferings()

		log.info 'Oci Training Offering refreshed'
	}

	void buildTriggers() {
		if (!Environment.developmentMode) {
			triggers << factory('OCI Training Trigger - run_once_immediately').noRepeat().build()
			triggers << factory('OCI Training Trigger - repeat every six hours').intervalInHours(6).build()
		}
	}
}
