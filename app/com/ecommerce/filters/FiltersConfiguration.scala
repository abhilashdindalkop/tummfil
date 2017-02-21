package filters;

import javax.inject.Inject
import play.api.http.HttpFilters

class FiltersConfiguration @Inject()(loggingFilter:com.ecommerce.filters.LoggingFilter) extends HttpFilters {
	val filters = Seq(loggingFilter)
}