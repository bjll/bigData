package com.test.filetohabse.queue;

import org.apache.hadoop.hbase.util.Sleeper;

/**
 * 测试用的job
 * 
 * @author Chris
 *
 */
public class Job {
	private String jobName;
	private String company;
	private String salary;
	private String palace;

	public Job() {

	}

	public Job(String jobName, String company, String salary, String palace) {
		this.jobName = jobName;
		this.company = company;
		this.salary = salary;
		this.palace = palace;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getPalace() {
		return palace;
	}

	public void setPalace(String palace) {
		this.palace = palace;
	}

	@Override
	public String toString() {
		return "Job [jobName=" + jobName + ", company=" + company + ", salary=" + salary + ", palace=" + palace + "]";
	}

}
