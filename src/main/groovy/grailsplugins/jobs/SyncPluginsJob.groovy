package grailsplugins.jobs

import grailsplugins.services.PluginService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton

@Slf4j
@Singleton
@CompileStatic
@Requires(notEnv='test')
class SyncPluginsJob {

	private final PluginService pluginService

	SyncPluginsJob(PluginService pluginService) {
		this.pluginService = pluginService
	}

	@Scheduled(fixedDelay = '${grailsplugins.sync.fixed-delay:1h}', initialDelay = '${grailsplugins.sync.initial-delay:10s}')
	void execute() {
		log.info 'Fetching latest plugin data'
		pluginService.refreshPluginRegistry()
	}
}
