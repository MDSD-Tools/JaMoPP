import React from 'react';
import { ChatContextProvider } from '/imports/ui/components/components-data/chat-context/context';
import { UsersContextProvider } from '/imports/ui/components/components-data/users-context/context';
import { GroupChatContextProvider } from '/imports/ui/components/components-data/group-chat-context/context';


const providersList = [
  ChatContextProvider,
  GroupChatContextProvider,
  UsersContextProvider,
];

const ContextProvidersComponent = props => providersList.reduce((acc, Component) => (
  <Component>
    {acc}
  </Component>), props.children);

export default ContextProvidersComponent;
