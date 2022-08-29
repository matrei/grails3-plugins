package grailsplugins

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

@CompileStatic
@Slf4j
class SyncPluginsJobService {

	static lazyInit = false

	GrailsPluginsService grailsPluginsService

	@Scheduled(fixedRate = 43200000L, initialDelay = 1000L) // 12 hour, delay 1s
	void execute() {
		log.info 'Fetching latest plugin data'
		grailsPluginsService.refresh()
	}
}
