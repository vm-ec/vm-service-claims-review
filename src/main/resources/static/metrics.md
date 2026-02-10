{
"timestamp": "2026-01-19T14:36:41.173538Z",
"epoch_timestamp": 1768833401,
"metrics": [
{
"name": "python_gc_objects_collected",
"documentation": "Objects collected during gc",
"type": "counter",
"samples": [
{
"name": "python_gc_objects_collected_total",
"labels": {
"generation": "0"
},
"value": 15618,
"timestamp": 1768833401.17351
},
{
"name": "python_gc_objects_collected_total",
"labels": {
"generation": "1"
},
"value": 757,
"timestamp": 1768833401.17351
},
{
"name": "python_gc_objects_collected_total",
"labels": {
"generation": "2"
},
"value": 96,
"timestamp": 1768833401.17351
}
]
},
{
"name": "python_gc_objects_uncollectable",
"documentation": "Uncollectable objects found during GC",
"type": "counter",
"samples": [
{
"name": "python_gc_objects_uncollectable_total",
"labels": {
"generation": "0"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "python_gc_objects_uncollectable_total",
"labels": {
"generation": "1"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "python_gc_objects_uncollectable_total",
"labels": {
"generation": "2"
},
"value": 0,
"timestamp": 1768833401.17351
}
]
},
{
"name": "python_gc_collections",
"documentation": "Number of times this generation was collected",
"type": "counter",
"samples": [
{
"name": "python_gc_collections_total",
"labels": {
"generation": "0"
},
"value": 395,
"timestamp": 1768833401.17351
},
{
"name": "python_gc_collections_total",
"labels": {
"generation": "1"
},
"value": 35,
"timestamp": 1768833401.17351
},
{
"name": "python_gc_collections_total",
"labels": {
"generation": "2"
},
"value": 3,
"timestamp": 1768833401.17351
}
]
},
{
"name": "python_info",
"documentation": "Python platform information",
"type": "gauge",
"samples": [
{
"name": "python_info",
"labels": {
"version": "3.13.1",
"implementation": "CPython",
"major": "3",
"minor": "13",
"patchlevel": "1"
},
"value": 1,
"timestamp": 1768833401.17351
}
]
},
{
"name": "ai_requests",
"documentation": "Total AI requests",
"type": "counter",
"samples": [
{
"name": "ai_requests_total",
"labels": {
"model": "gemini",
"operation": "classification"
},
"value": 85,
"timestamp": 1768833401.17351
},
{
"name": "ai_requests_created",
"labels": {
"model": "gemini",
"operation": "classification"
},
"value": 1768535855.38981,
"timestamp": 1768833401.17351
}
]
},
{
"name": "ai_request_duration_seconds",
"documentation": "AI request latency",
"type": "histogram",
"samples": [
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.005"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.01"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.025"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.05"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.075"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.1"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.25"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.5"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "0.75"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "1.0"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "2.5"
},
"value": 0,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "5.0"
},
"value": 67,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "7.5"
},
"value": 79,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "10.0"
},
"value": 85,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_bucket",
"labels": {
"model": "gemini",
"operation": "classification",
"le": "+Inf"
},
"value": 85,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_count",
"labels": {
"model": "gemini",
"operation": "classification"
},
"value": 85,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_sum",
"labels": {
"model": "gemini",
"operation": "classification"
},
"value": 367.130332231522,
"timestamp": 1768833401.17351
},
{
"name": "ai_request_duration_seconds_created",
"labels": {
"model": "gemini",
"operation": "classification"
},
"value": 1768535859.6162,
"timestamp": 1768833401.17351
}
]
},
{
"name": "ai_tokens",
"documentation": "Total AI tokens used",
"type": "counter",
"samples": [
{
"name": "ai_tokens_total",
"labels": {
"model": "gemini",
"type": "prediction_input"
},
"value": 112591,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_created",
"labels": {
"model": "gemini",
"type": "prediction_input"
},
"value": 1768535859.61648,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_total",
"labels": {
"model": "gemini",
"type": "prediction_output"
},
"value": 391,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_created",
"labels": {
"model": "gemini",
"type": "prediction_output"
},
"value": 1768535859.61653,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_total",
"labels": {
"model": "gemini",
"type": "total_input"
},
"value": 118762,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_created",
"labels": {
"model": "gemini",
"type": "total_input"
},
"value": 1768535859.61657,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_total",
"labels": {
"model": "gemini",
"type": "total_output"
},
"value": 4736,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_created",
"labels": {
"model": "gemini",
"type": "total_output"
},
"value": 1768535859.6166,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_total",
"labels": {
"model": "gemini",
"type": "summary_input"
},
"value": 6171,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_created",
"labels": {
"model": "gemini",
"type": "summary_input"
},
"value": 1768535859.61673,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_total",
"labels": {
"model": "gemini",
"type": "summary_output"
},
"value": 4345,
"timestamp": 1768833401.17351
},
{
"name": "ai_tokens_created",
"labels": {
"model": "gemini",
"type": "summary_output"
},
"value": 1768535859.61677,
"timestamp": 1768833401.17351
}
]
},
{
"name": "ai_errors",
"documentation": "Total AI errors",
"type": "counter",
"samples": []
},
{
"name": "ai_confidence_score",
"documentation": "AI model confidence scores",
"type": "histogram",
"samples": []
},
{
"name": "benchmark_runs",
"documentation": "Total benchmark runs",
"type": "counter",
"samples": [
{
"name": "benchmark_runs_total",
"labels": {

          },
          "value": 17,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_runs_created",
          "labels": {

          },
          "value": 1768535853.62919,
          "timestamp": 1768833401.17351
        }
      ]
    },
    {
      "name": "benchmark_accuracy_percent",
      "documentation": "Current benchmark accuracy percentage",
      "type": "gauge",
      "samples": [
        {
          "name": "benchmark_accuracy_percent",
          "labels": {

          },
          "value": 100,
          "timestamp": 1768833401.17351
        }
      ]
    },
    {
      "name": "benchmark_test_duration_seconds",
      "documentation": "Time taken for benchmark tests",
      "type": "histogram",
      "samples": [
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.005"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.01"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.025"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.05"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.075"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.1"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.25"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.5"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "0.75"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "1.0"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "2.5"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "5.0"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "7.5"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "10.0"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_bucket",
          "labels": {
            "le": "+Inf"
          },
          "value": 17,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_count",
          "labels": {

          },
          "value": 17,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_sum",
          "labels": {

          },
          "value": 2476.32326984406,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_test_duration_seconds_created",
          "labels": {

          },
          "value": 1768535853.62926,
          "timestamp": 1768833401.17351
        }
      ]
    },
    {
      "name": "benchmark_classification_results",
      "documentation": "Benchmark classification results",
      "type": "counter",
      "samples": [
        {
          "name": "benchmark_classification_results_total",
          "labels": {
            "result_type": "correct"
          },
          "value": 85,
          "timestamp": 1768833401.17351
        },
        {
          "name": "benchmark_classification_results_created",
          "labels": {
            "result_type": "correct"
          },
          "value": 1768535862.50446,
          "timestamp": 1768833401.17351
        }
      ]
    },
    {
      "name": "http_requests",
      "documentation": "Total HTTP requests",
      "type": "counter",
      "samples": []
    },
    {
      "name": "document_processing_seconds",
      "documentation": "Document processing time",
      "type": "histogram",
      "samples": [
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.005"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.01"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.025"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.05"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.075"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.1"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.25"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.5"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "0.75"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "1.0"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "2.5"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "5.0"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "7.5"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "10.0"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_bucket",
          "labels": {
            "le": "+Inf"
          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_count",
          "labels": {

          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_sum",
          "labels": {

          },
          "value": 0,
          "timestamp": 1768833401.17351
        },
        {
          "name": "document_processing_seconds_created",
          "labels": {

          },
          "value": 1768535855.35062,
          "timestamp": 1768833401.17351
        }
      ]
    },
    {
      "name": "documents_processed",
      "documentation": "Total documents processed",
      "type": "counter",
      "samples": []
    }
],
"prometheus_queries": {
"average_latency": "rate(ai_request_duration_seconds_sum[5m]) / rate(ai_request_duration_seconds_count[5m])",
"p95_latency": "histogram_quantile(0.95, rate(ai_request_duration_seconds_bucket[5m]))",
"p99_latency": "histogram_quantile(0.99, rate(ai_request_duration_seconds_bucket[5m]))",
"request_rate": "rate(ai_requests_total[5m])",
"error_rate": "rate(ai_errors_total[5m]) / rate(ai_requests_total[5m])"
}
}