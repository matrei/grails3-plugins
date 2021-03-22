package grailsplugins

import com.bintray.BintrayPackage
import com.github.GithubRepository
import groovy.transform.CompileStatic

@CompileStatic
interface GrailsPluginsRepository {
    /**
     * @deprecated We are no longer using Bintray API.
     */
    @Deprecated
    BintrayKey save(BintrayPackage bintrayPackage)
    BintrayKey save(GrailsPlugin plugin)
    BintrayKey updateGithubRepository(BintrayKey key, GithubRepository githubRepository)
    BintrayKey updateGithubRepositoryReadme(BintrayKey key, String readme)
    GrailsPlugin find(BintrayKey key)
    int count()
    List<GrailsPlugin> topRatedPlugins()
    List<GrailsPlugin> latestPlugins()
    List<GrailsPlugin> findAll()
    List<GrailsPlugin> findByQuery(String query)
    GrailsPlugin findByPluginName(String name)
    String findPreviousLatestVersion(BintrayKey key)
    void clearNotUpdatedSince(Date date)
}
