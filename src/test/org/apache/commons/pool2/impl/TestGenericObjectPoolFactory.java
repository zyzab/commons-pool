/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.pool2.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.apache.commons.pool2.MethodCallPoolableObjectFactory;
import org.apache.commons.pool2.ObjectPoolFactory;
import org.apache.commons.pool2.PoolableObjectFactory;
import org.apache.commons.pool2.TestObjectPoolFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolFactory;
import org.apache.commons.pool2.impl.WhenExhaustedAction;
import org.junit.Test;

/**
 * Tests for {@link GenericObjectPoolFactory}.
 *
 * @author Sandy McArthur
 * @version $Revision$ $Date$
 */
public class TestGenericObjectPoolFactory extends TestObjectPoolFactory {
    @Override
    protected ObjectPoolFactory<Object> makeFactory(final PoolableObjectFactory<Object> objectFactory) throws UnsupportedOperationException {
        return new GenericObjectPoolFactory<Object>(objectFactory);
    }

    @Test
    public void testConstructors() throws Exception {
        GenericObjectPoolFactory<Object> factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory());
        GenericObjectPool<Object> pool;
        factory.createPool().close();

        GenericObjectPoolConfig config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setMaxIdle(2)
            .setMaxWait(3)
            .setMinIdle(4)
            .setMinEvictableIdleTimeMillis(5)
            .setNumTestsPerEvictionRun(6)
            .setSoftMinEvictableIdleTimeMillis(7)
            .setTestOnBorrow(true)
            .setTestOnReturn(false)
            .setTestWhileIdle(true)
            .setLifo(false)
            .setTimeBetweenEvictionRunsMillis(8)
            .setWhenExhaustedAction(WhenExhaustedAction.GROW)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(2, pool.getMaxIdle());
        assertEquals(3, pool.getMaxWait());
        assertEquals(4, pool.getMinIdle());
        assertEquals(5, pool.getMinEvictableIdleTimeMillis());
        assertEquals(6, pool.getNumTestsPerEvictionRun());
        assertEquals(7, pool.getSoftMinEvictableIdleTimeMillis());
        assertTrue(pool.getTestOnBorrow());
        assertFalse(pool.getTestOnReturn());
        assertTrue(pool.getTestWhileIdle());
        assertFalse(pool.getLifo());
        assertEquals(8, pool.getTimeBetweenEvictionRunsMillis());
        assertEquals(WhenExhaustedAction.GROW, pool.getWhenExhaustedAction());
        pool.borrowObject();
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        pool.borrowObject();
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setWhenExhaustedAction(WhenExhaustedAction.BLOCK)
            .setMaxWait(125)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(WhenExhaustedAction.BLOCK, pool.getWhenExhaustedAction());
        assertEquals(125, pool.getMaxWait());
        pool.borrowObject();
        long startTime = System.currentTimeMillis();
        try {
            pool.borrowObject();
            fail();
        } catch (NoSuchElementException nsee) {
            // expected
        }
        long delay = System.currentTimeMillis() - startTime;
        assertTrue("delay: " + delay, delay > 100);
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setWhenExhaustedAction(WhenExhaustedAction.GROW)
            .setMaxWait(2)
            .setTestOnBorrow(true)
            .setTestOnReturn(false)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(2, pool.getMaxWait());
        assertTrue(pool.getTestOnBorrow());
        assertFalse(pool.getTestOnReturn());
        assertEquals(WhenExhaustedAction.GROW, pool.getWhenExhaustedAction());
        pool.borrowObject();
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setWhenExhaustedAction(WhenExhaustedAction.GROW)
            .setMaxWait(2)
            .setMaxIdle(3)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(2, pool.getMaxWait());
        assertEquals(3, pool.getMaxIdle());
        assertEquals(WhenExhaustedAction.GROW, pool.getWhenExhaustedAction());
        pool.borrowObject();
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setWhenExhaustedAction(WhenExhaustedAction.GROW)
            .setMaxWait(2)
            .setMaxIdle(3)
            .setTestOnBorrow(true)
            .setTestOnReturn(false)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(2, pool.getMaxWait());
        assertEquals(3, pool.getMaxIdle());
        assertTrue(pool.getTestOnBorrow());
        assertFalse(pool.getTestOnReturn());
        assertEquals(WhenExhaustedAction.GROW, pool.getWhenExhaustedAction());
        pool.borrowObject();
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setWhenExhaustedAction(WhenExhaustedAction.GROW)
            .setMaxWait(2)
            .setMaxIdle(3)
            .setTimeBetweenEvictionRunsMillis(4)
            .setNumTestsPerEvictionRun(5)
            .setMinEvictableIdleTimeMillis(6)
            .setTestOnBorrow(true)
            .setTestOnReturn(false)
            .setTestWhileIdle(false)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(2, pool.getMaxWait());
        assertEquals(3, pool.getMaxIdle());
        assertEquals(4, pool.getTimeBetweenEvictionRunsMillis());
        assertEquals(5, pool.getNumTestsPerEvictionRun());
        assertEquals(6, pool.getMinEvictableIdleTimeMillis());
        assertTrue(pool.getTestOnBorrow());
        assertFalse(pool.getTestOnReturn());
        assertFalse(pool.getTestWhileIdle());
        assertEquals(WhenExhaustedAction.GROW, pool.getWhenExhaustedAction());
        pool.borrowObject();
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setMaxWait(2)
            .setMaxIdle(3)
            .setMinIdle(4)
            .setTimeBetweenEvictionRunsMillis(5)
            .setNumTestsPerEvictionRun(6)
            .setMinEvictableIdleTimeMillis(7)
            .setTestOnBorrow(true)
            .setTestOnReturn(false)
            .setTestWhileIdle(true)
            .setWhenExhaustedAction(WhenExhaustedAction.GROW)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(2, pool.getMaxWait());
        assertEquals(3, pool.getMaxIdle());
        assertEquals(4, pool.getMinIdle());
        assertEquals(5, pool.getTimeBetweenEvictionRunsMillis());
        assertEquals(6, pool.getNumTestsPerEvictionRun());
        assertEquals(7, pool.getMinEvictableIdleTimeMillis());
        assertTrue(pool.getTestOnBorrow());
        assertFalse(pool.getTestOnReturn());
        assertTrue(pool.getTestWhileIdle());
        assertEquals(WhenExhaustedAction.GROW, pool.getWhenExhaustedAction());
        pool.borrowObject();
        pool.close();


        config = new GenericObjectPoolConfig.Builder()
            .setMaxTotal(1)
            .setMaxWait(2)
            .setMaxIdle(3)
            .setMinIdle(4)
            .setTimeBetweenEvictionRunsMillis(5)
            .setNumTestsPerEvictionRun(6)
            .setMinEvictableIdleTimeMillis(7)
            .setSoftMinEvictableIdleTimeMillis(8)
            .setTestOnBorrow(true)
            .setTestOnReturn(false)
            .setTestWhileIdle(true)
            .setLifo(false)
            .setWhenExhaustedAction(WhenExhaustedAction.GROW)
            .createConfig();
        factory = new GenericObjectPoolFactory<Object>(new MethodCallPoolableObjectFactory(), config);
        pool = (GenericObjectPool<Object>)factory.createPool();
        assertEquals(1, pool.getMaxTotal());
        assertEquals(2, pool.getMaxWait());
        assertEquals(3, pool.getMaxIdle());
        assertEquals(4, pool.getMinIdle());
        assertEquals(5, pool.getTimeBetweenEvictionRunsMillis());
        assertEquals(6, pool.getNumTestsPerEvictionRun());
        assertEquals(7, pool.getMinEvictableIdleTimeMillis());
        assertEquals(8, pool.getSoftMinEvictableIdleTimeMillis());
        assertTrue(pool.getTestOnBorrow());
        assertFalse(pool.getTestOnReturn());
        assertTrue(pool.getTestWhileIdle());
        assertFalse(pool.getLifo());
        assertEquals(WhenExhaustedAction.GROW, pool.getWhenExhaustedAction());
        pool.borrowObject();
        pool.close();
    }
}
