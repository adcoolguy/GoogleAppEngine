package com.bkper.server.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;

@Api(name = "bkper")
public class TransactionEndpoint {

	public List<TempTransaction> listTransaction() {

		TempTransaction tempTransaction = new TempTransaction();
		tempTransaction.setDescription("lalalala");
		tempTransaction.setInformedDate(new Date());

		List<TempTransaction> transactions = new ArrayList<TempTransaction>();
		transactions.add(tempTransaction);

//		Persistence.getManager().setDetachAllOnCommit(true);		//for cloud endpoints

		return transactions;

	}
}