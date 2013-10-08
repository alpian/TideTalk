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
package jetbrains.communicator.core.transport;

import com.intellij.openapi.util.text.StringUtil;
import jetbrains.communicator.core.EventBroadcaster;
import org.jdom.Element;

/**
 * @author Kir
 */
public class TextMessageEventProvider extends EventProvider {

  public TextMessageEventProvider(EventBroadcaster broadcaster) {
    super(broadcaster);
  }

  @Override
  public String getTagName() {
    return TextXmlMessage.TAG;
  }

  @Override
  protected TransportEvent createEvent(Transport transport, String remoteUser, Element rootElement) {
    if (!StringUtil.isEmptyOrSpaces(rootElement.getText())) {
      return EventFactory.createMessageEvent(transport, remoteUser, rootElement.getText());
    }
    return null;
  }
}
