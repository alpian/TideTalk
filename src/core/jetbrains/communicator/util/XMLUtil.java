/*
 * Copyright 2000-2006 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.communicator.util;

import com.thoughtworks.xstream.XStream;
import jetbrains.communicator.core.Pico;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NonNls;

import java.io.*;

/**
 * @author Kir Maximov
 */
public class XMLUtil {
  @NonNls
  private static final Logger LOG = Logger.getLogger(XMLUtil.class);

  private XMLUtil() {
  }

  public static XStream createXStream() {
    final XStream xStream = new XStream();
    xStream.setClassLoader(XMLUtil.class.getClassLoader());
    return xStream;
  }

  public static Object fromXml(XStream xStream, File dir, String fileName, boolean reportFailureAsError) {
    return fromXml(xStream, new File(dir, fileName).getAbsolutePath(), reportFailureAsError);
  }

  public static Object fromXml(XStream xStream, String fullFileName, boolean reportFailureAsError) {
    Reader fileReader = null;
    try {
      BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fullFileName));
      fileReader = new InputStreamReader(inputStream, "UTF-8");
      return xStream.fromXML(fileReader);
    }
    catch (FileNotFoundException ignored) {
      return null;
    }
    catch (Throwable e) {
      if (reportFailureAsError) {
        LOG.error("Error reading " + fullFileName, e);
      }
      else {
        LOG.info("Error reading " + fullFileName, e);
      }
      return null;
    }
    finally {
      if (fileReader != null) {
        try {
          fileReader.close();
        }
        catch (IOException e) {
          processError(e);
        }
      }
    }
  }

  public static void toXml(XStream xStream, File dir, String fileName, Object object) {
    toXml(xStream, new File(dir, fileName).getAbsolutePath(), object);
  }

  public static void toXml(XStream xStream, String fullFileName, Object object) {
    Writer writer = null;
    try {
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fullFileName));
      writer = new OutputStreamWriter(stream, "UTF-8");
      xStream.toXML(object, writer);
    } catch (Exception e) {
      processError(e);
    }
    finally{
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          processError(e);
        }
      }
    }
  }

  private static void processError(Exception e) {
    LOG.error(e.getMessage());
    LOG.info(e.getMessage(), e);
    if (Pico.isUnitTest()) {
      assert false: "LOG.error()";
    }
  }
}