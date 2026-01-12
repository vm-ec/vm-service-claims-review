//package com.vm.service.claimsreview.service;
//
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class ObservabilityService {
//    public Map<String, String> getObservabilityContext() {
//        // Implementation to retrieve observability context
//        return Map.of("metrics", getSampledata()); // Placeholder implementation
//    }
//    public Map<String , String> getUserContext() {
//        // Implementation to retrieve user context
//        return Map.of(); // Placeholder implementation
//    }
//    private String getSampledata(){
//        String json ="{'meta':{'timestamp':'2024-10-24T14:30:00Z','kpis':[{'id':'total_inf','title':'Total Inferences','value':'4.2M','trend':'▲ 8% vs last week','trendType':'good'},{'id':'avg_lat','title':'Avg Latency (P95)','value':'520ms','trend':'▼ 12ms improvement','trendType':'good'},{'id':'cost','title':'Projected Cost','value':'$18,240','trend':'▲ 2% Over Budget','trendType':'warn','isFinancial':true},{'id':'err_rate','title':'Error Rate','value':'0.12%','trend':'Stable','trendType':'neutral'}]},'columns':[{'key':'name','label':'Application Name','type':'identity'},{'key':'status','label':'Status','type':'status'},{'key':'tech','label':'Technology','type':'text'},{'key':'latency','label':'Latency (P95)','type':'metric'},{'key':'requests','label':'Requests','type':'metric'},{'key':'drift','label':'Drift Score','type':'number'},{'key':'cost','label':'Cost (24h)','type':'financial'}],'rows':[{'id':1,'name':'Policy CoPilot','status':'Healthy','tech':'LLM','model':'GPT-4o','latency':'850ms','requests':'1.2M','drift':0.02,'cost':'$420.00','logs':['Query sanitized','Response generated']},{'id':2,'name':'Fraud Detector','status':'Critical','tech':'ML','model':'XGBoost','latency':'120ms','requests':'8.5M','drift':0.45,'cost':'$80.00','logs':['CRITICAL: Drift detected']}]}";
//        return json;
//    }
//}