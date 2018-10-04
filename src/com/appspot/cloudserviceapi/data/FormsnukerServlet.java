package com.appspot.cloudserviceapi.data;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

/**
 * http://stackoverflow.com/questions/108822/delete-all-data-for-a-kind-in-google-app-engine
 */
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.model.Constants;
import com.appspot.cloudserviceapi.sgc.Protect;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

@SuppressWarnings("serial")
public class FormsnukerServlet extends HttpServlet {

	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		response.setContentType("text/plain");

		final String kind = request.getParameter("kind");
		// final String passcode = request.getParameter("passcode");

		if (kind == null) {
			throw new IOException("Kind is NULL or empty.");
		}

		if (Protect.isAdmin()) {
			System.err.println("*** deleting entities form " + kind);

			final long start = System.currentTimeMillis();

			int deleted_count = 0;
			boolean is_finished = false;

			final DatastoreService dss = DatastoreServiceFactory
					.getDatastoreService();

			while (System.currentTimeMillis() - start < 16384) {

				final Query query = new Query(kind);

				query.setKeysOnly();

				final ArrayList<Key> keys = new ArrayList<Key>();

				for (final Entity entity : dss.prepare(query).asIterable(
						FetchOptions.Builder.withLimit(128))) {
					keys.add(entity.getKey());
				}

				keys.trimToSize();

				if (keys.size() == 0) {
					is_finished = true;
					break;
				}

				while (System.currentTimeMillis() - start < 16384) {

					try {

						dss.delete(keys);

						deleted_count += keys.size();

						break;

					} catch (Throwable ignore) {

						continue;

					}

				}

			}

			System.err.println("*** deleted " + deleted_count
					+ " entities form " + kind);

			if (is_finished) {

				System.err.println("*** deletion job for " + kind
						+ " is completed.");

			} else {

				final int taskcount;

				final String tcs = request.getParameter("taskcount");

				if (tcs == null) {
					taskcount = 0;
				} else {
					taskcount = Integer.parseInt(tcs) + 1;
				}

				QueueFactory.getDefaultQueue().add(
						withUrl(
								"/formsnuker?kind=" + kind
										+ "&passcode=LONGSECRETCODE&taskcount="
										+ taskcount).method(Method.GET));

				System.err.println("*** deletion task # " + taskcount + " for "
						+ kind + " is queued.");

			}
			response.getWriter().println("Datastore purged!");
		} else {
			response.getWriter().println(Constants.NO_RIGHT_TO + "purge datastore!");
		}
	}

}