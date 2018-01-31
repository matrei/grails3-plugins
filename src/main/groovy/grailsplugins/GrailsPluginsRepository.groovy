package grailsplugins

import com.bintray.BintrayPackage
import com.github.GithubRepository
import groovy.transform.CompileStatic

@CompileStatic
interface GrailsPluginsRepository {
    BintrayKey save(BintrayPackage bintrayPackage)
    BintrayKey updateGithubRepository(BintrayKey key, GithubRepository githubRepository)
    BintrayKey updateGithubRepositoryReadme(BintrayKey key, String readme)
    GrailsPlugin find(BintrayKey key)
    int count()
    void clear()
    List<GrailsPlugin> topRatedPlugins()
    List<GrailsPlugin> latestPlugins()
    List<GrailsPlugin> findAll()
    List<GrailsPlugin> findByQuery(String query)
    GrailsPlugin findByPluginName(String name)
    String findPreviousLatestVersion(BintrayKey key)
}