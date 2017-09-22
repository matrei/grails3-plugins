<g:if test="${plugin.githubRepo}">
    <div class="githubstar">
        <a href="${plugin.githubRepo.html_url}/stargazers">
            <span class="star"><asset:image src="small_githubstar.svg" height="20"/></span>
            <span class="count">${plugin.githubRepo.stargazers_count}</span>
        </a>
    </div>
</g:if>
