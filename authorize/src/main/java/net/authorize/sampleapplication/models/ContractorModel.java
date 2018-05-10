package net.authorize.sampleapplication.models;

import net.authorize.sampleapplication.IContractor;
import net.authorize.sampleapplication.SubscriptionCheckListener;

/**
 * Created by fazal on 12/29/2017.
 */

public class ContractorModel {

    private static ContractorModel mInstance;

    IContractor contractor;

    SubscriptionCheckListener subscription;

    private ContractorModel() {
    }

    public static ContractorModel getInstance() {
        if(mInstance == null) {
            mInstance = new ContractorModel();
        }
        return mInstance;
    }

    public void setContractor(IContractor contractor) {
        this.contractor = contractor;
    }

    public void setSubscription(SubscriptionCheckListener subscription) {
        this.subscription = subscription;
    }

    public void sendData(float totalammount){
        if(StaticData.isannual.equalsIgnoreCase("True")){
            contractor.sendDataPurchase("Annual", totalammount);
        }else {
            contractor.sendDataPurchase("Quarter", totalammount);
        }
    }

    public void sendDataPayment(String status){
        subscription.onSubscribe(status);
    }
}
