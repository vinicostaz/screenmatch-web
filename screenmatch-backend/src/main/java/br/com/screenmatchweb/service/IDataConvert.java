package br.com.screenmatchweb.service;

public interface IDataConvert {
    <T> T  getData(String json, Class<T> tClass);
}