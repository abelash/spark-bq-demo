package org.abelash.demo


/**
 * Hello world!
 *
 */
/*
gcloud dataproc jobs submit spark --cluster "$MY_CLUSTER" \
  --jar=target/abe-spark-bq-demo-0.1.jar \
  --jars gs://spark-lib/bigquery/spark-bigquery-latest.jar
*/
object SparkBQConnector  {

  def main(args: Array[String]) {
    println("Hello World!")

    /*
 * Remove comment if you are not running in spark-shell.
 * */
    import org.apache.spark.sql.SparkSession
    val spark = SparkSession.builder()
      .appName("spark-bigquery-demo")
      .getOrCreate()

    // Use the Cloud Storage bucket for temporary BigQuery export data used
    // by the connector.
    val bucket = "abe-bq-load"
    spark.conf.set("temporaryGcsBucket", bucket)

    // Load data in from BigQuery.
    val wordsDF = spark.read.format("bigquery")
      .option("table", "publicdata.samples.shakespeare")
      .load()
      .cache()
    wordsDF.createOrReplaceTempView("words")

    // Perform word count.
    val wordCountDF = spark.sql(
      "SELECT word, SUM(word_count) AS word_count FROM words GROUP BY word")
    wordCountDF.show()
    wordCountDF.printSchema()

    println("Starting saving to BQ")
    // Saving the data to BigQuery.
    wordCountDF.write
      .format("bigquery")
      .option("table", "wordcount_dataset.wordcount_output")
      .save()
    println("Saving to BQ COMPLETED")
  }
}
